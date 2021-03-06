(ns vit-configurator.events
  (:require [clojure.string :as str]
            [re-frame.core :as re-frame]
            [vit-configurator.db :as db]
            [vit-configurator.subs :as subs]))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-logo
 (fn [db [_ type optional-value]]
   (if (#{:state-seal :custom} type)
     (assoc db :logo {:type type :value optional-value})
     (assoc db :logo {:type type}))))

(re-frame/reg-event-db
 ::set-title
 (fn [db [_ title]]
   (if (str/blank? title)
     (dissoc db :title)
     (assoc db :title title))))

(re-frame/reg-event-db
 ::set-voter-info
 (fn [db [_ voter-info]]
   (if (str/blank? voter-info)
     (dissoc db :voter-info)
     (assoc db :voter-info voter-info))))

(re-frame/reg-event-db
 ::set-language
 (fn [db [_ lang]]
   (assoc db :language lang)))

(re-frame/reg-event-db
 ::set-official-only
 (fn [db [_ official]]
   (assoc db :official-only official)))

(re-frame/reg-event-db
 ::set-link-text
 (fn [db [_ link-kw text]]
   (if (str/blank? text)
     (update db :links dissoc link-kw)
     (assoc-in db [:links link-kw] text))))

(re-frame/reg-event-db
 ::set-size
 (fn [db [_ size]]
   (assoc db :size size)))

(re-frame/reg-event-fx
 ::preview-ready
 (fn [{:keys [db]} [_ preview]]
   {:db (-> db
            (assoc :preview-ready true)
            (assoc :preview-window preview))
    :dispatch [::send-config]}))

(re-frame/reg-event-fx
 ::send-config
 (fn [{:keys [db]} _]
   (when (:preview-ready db)
     (let [preview-window (:preview-window db)
           config (:config db)
           desktop-config (merge config {:display-mode "desktop"})
           event (clj->js {"config" desktop-config})]
       (.postMessage preview-window event)))
   {:db db}))

(re-frame/reg-event-db
 ::reload-preview
 (fn [db _]
   (let [preview-window (:preview-window db)
         location (.-location preview-window)]
     (.reload location)
     db)))

(defn add-language-code
  [m k]
  (if-let [v (get m k nil)]
    (-> m
        (dissoc k)
        (assoc-in (concat [k] [:en]) v))
    m))

(defn clj-config
  [db]
  (as->
      (select-keys db [:logo :language :size :title :links :voter-info :official-only]) $
      (add-language-code $ :title)
      (add-language-code $ :voter-info)
      (add-language-code $ :links)))

(defn save-config-and-reload-preview
  [{:keys [db]} _]
  {:db (assoc db :config (clj-config db))
   :dispatch [::reload-preview]})

(re-frame/reg-event-fx ::save-config-and-reload-preview save-config-and-reload-preview)
