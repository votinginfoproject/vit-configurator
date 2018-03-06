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
