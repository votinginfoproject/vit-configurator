(ns vit-configurator.subs
  (:require [clojure.string :as str]
            [re-frame.core :as re-frame]))

(re-frame/reg-sub ::title :title)
(re-frame/reg-sub ::alert :alert)
(re-frame/reg-sub ::logo :logo)
(re-frame/reg-sub ::language :language)
(re-frame/reg-sub ::official-data-only :official-data-only)
(re-frame/reg-sub ::links :links)
(re-frame/reg-sub ::size :size)

(defn config
  [db _]
  (->> (select-keys db [:logo :language :size :title :links :alert])
       clj->js
       (.stringify js/JSON)))
(re-frame/reg-sub ::config config)
