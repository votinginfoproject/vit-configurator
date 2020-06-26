(ns vit-configurator.views.select-view
  (:require [clojure.string :as str]
            [re-frame.core :as re-frame]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]))

(defn option
  [[kw text]]
  [:option {:value kw :key (name kw)} text])

(defn select-card
  "Generic framework for a card/view that presents a select box of options.

  title-text: the string for the title of the card
  description-text: optional instructions/description of the feature
  subscription: a re-frame subscription containing the current value
  event-kw: the event to be dispatched when the value changes
  options: a vector of tuples for the options, with the first element being a keyword signifying the option, and the second being the text to display to the user

  When a change is made, we dispatch the event using the supplied event keyword and send the first element from the tuple of the corresponding option."
  [{:keys [title-text description-text subscription event-kw options]}]
  (let [current-val @subscription]
    [:div
     [:p.mb-1 title-text]
     (when-not (str/blank? description-text)
       [:p.mb-1.small {:dangerouslySetInnerHTML {:__html description-text}}])
     [:select.mb-2 {:value current-val
                    :on-change (fn [selection]
                                 (let [new-val (-> selection
                                                   (.. -target -value)
                                                   keyword)]
                                   (re-frame/dispatch
                                    [event-kw new-val])))}
      (map option options)]]))
