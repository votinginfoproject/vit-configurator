(ns vit-configurator.subs
  (:require [clojure.string :as str]
            [re-frame.core :as re-frame]))

(defn get-or-nil
  [kw]
  (fn [db _] (get db kw nil)))

(re-frame/reg-sub ::title (get-or-nil :title))
(re-frame/reg-sub ::voter-info (get-or-nil :voter-info))
(re-frame/reg-sub ::logo (get-or-nil :logo))
(re-frame/reg-sub ::language (get-or-nil :language))
(re-frame/reg-sub ::official-only (get-or-nil :official-only))
(re-frame/reg-sub ::links (get-or-nil :links))
(re-frame/reg-sub ::size (get-or-nil :size))

(re-frame/reg-sub ::clj-config :config)

(defn js-config
  [db _]
  (-> (:config db)
      clj->js
      (.stringify js/JSON)))
(re-frame/reg-sub ::js-config js-config)
