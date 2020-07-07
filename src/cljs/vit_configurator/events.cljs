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
     (assoc-in db [:title :en] title))))

(re-frame/reg-event-db
 ::set-alert
 (fn [db [_ alert]]
   (if (str/blank? alert)
     (dissoc db :alert)
     (assoc-in db [:alert :en] alert))))

(re-frame/reg-event-db
 ::set-language
 (fn [db [_ lang]]
   (assoc db :language lang)))

(re-frame/reg-event-db
 ::set-official-data-only
 (fn [db [_ official]]
   (assoc db :official-data-only official)))

(re-frame/reg-event-db
 ::set-link-text
 (fn [db [_ link-kw text]]
   (if (str/blank? text)
     (update-in db [:links :en] dissoc link-kw)
     (assoc-in db [:links :en link-kw] text))))

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
    :dispatch [::send-config (subs/config db nil)]}))

(re-frame/reg-event-fx
 ::send-config
 (fn [{:keys [db]} [_ config]]
   (when (:preview-ready db)
     (let [preview-window (:preview-window db)
           event (clj->js {"config" config})]
       (.postMessage preview-window event)))
   {:db db}))

(re-frame/reg-event-db
 ::reload-preview
 (fn [db _]
   (let [preview-window (:preview-window db)
         location (.-location preview-window)]
     (.reload location)
     db)))
