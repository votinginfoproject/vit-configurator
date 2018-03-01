(ns vit-configurator.events
  (:require [re-frame.core :as re-frame]
            [vit-configurator.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))
