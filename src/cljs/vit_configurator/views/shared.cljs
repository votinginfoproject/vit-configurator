(ns vit-configurator.views.shared
  (:require [re-frame.core :as re-frame]))

(defn max-length-change-fn
  [max-length comp-state-atom event-vec]
  (fn [evt]
    (let [val (.. evt -target -value)]
      (reset! comp-state-atom val)
      (when (>= (- max-length (count val)) 0)
        (re-frame/dispatch (conj event-vec val))))))

(defn countdown
  [diff]
  [:p.small (when (neg? diff)
              {:class "text-danger"})
   (if (>= diff 0)
     (str "(" diff " characters remaining)")
     (str "(" (Math/abs diff) " characters over)"))])

(defn border-class
  [diff]
  (when (neg? diff)
    {:class "border-danger"}))
