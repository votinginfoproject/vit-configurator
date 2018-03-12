(ns vit-configurator.subs
  (:require [re-frame.core :as re-frame]))

(defn extract
  "Takes vararg keywords/symbols that will form a keypath to
  pull values from the db.
  e.g.
  (extract-path :foo)
  (extract-path :foo :bar first"
  [& path]
  (fn [db]
    (get-in db path nil)))

(re-frame/reg-sub ::logo (extract :logo))
(re-frame/reg-sub ::title (extract :title))
(re-frame/reg-sub ::intro (extract :intro))
(re-frame/reg-sub ::theme (extract :theme))
(re-frame/reg-sub ::language (extract :language))
(re-frame/reg-sub ::official-data-only (extract :official-data-only))
(re-frame/reg-sub ::links (extract :links))
