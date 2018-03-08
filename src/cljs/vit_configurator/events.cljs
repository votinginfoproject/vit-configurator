(ns vit-configurator.events
  (:require [re-frame.core :as re-frame]
            [vit-configurator.db :as db]))

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
   (assoc db :title title)))

(re-frame/reg-event-db
 ::set-intro
 (fn [db [_ intro]]
   (assoc db :intro intro)))

(re-frame/reg-event-db
 ::set-language
 (fn [db [_ lang]]
   (assoc db :language lang)))
