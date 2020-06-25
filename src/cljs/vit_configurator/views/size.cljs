(ns vit-configurator.views.size
  (:require [re-frame.core :as re-frame]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]
            [vit-configurator.views.select-view :as sel-v]))

(defn customizer
  []
  (let [options {:title-text "Pick a width for the embeddable tool."
                 :description-text "The recommended setting is the Flexible option, as it should work for both mobile and desktop viewings, and can size itself to fit into a containing column, provided the column dimensions are at least 320px wide. The two Fixed options can be selected if you want a more consistent experience for all users without the tool expanding or contracting to fit in with other page elements. These settings set styles on the containing HTML DIV element, which you could alter to suit your needs, however we do not recommend letting the width get smaller than 320px."
                 :subscription (re-frame/subscribe [::subs/size])
                 :event-kw ::events/set-size
                 :options [[:responsive "Flexible between 320 to 640 wide"]
                           [:small "Fixed 320 wide"]
                           [:regular "Fixed 640 wide"]]}]
    (sel-v/select-card options)))
