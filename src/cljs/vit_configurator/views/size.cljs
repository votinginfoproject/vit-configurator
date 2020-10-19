(ns vit-configurator.views.size
  (:require [re-frame.core :as re-frame]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]
            [vit-configurator.views.select-view :as sel-v]))

(defn customizer
  []
  (let [options {:title-text "Pick a width for the embeddable tool."
                 :description-text "<strong>The recommended setting is either of the Flexible options, as they should work for both mobile and desktop viewings, and can size itself to fit into a containing column, provided the column dimensions are at least 360px wide. The second Flexible option can expand wider, so if your layout has a lot of space, it is a good choice.</strong><br/> The two Fixed options can be selected if you want a more consistent experience for all users without the tool expanding or contracting to fit in with other page elements. These settings set styles on the containing HTML DIV element, which you could alter to suit your needs, however we do not recommend letting the width get smaller than 360px."
                 :subscription (re-frame/subscribe [::subs/size])
                 :event-kw ::events/set-size
                 :options [[:responsive "Flexible between 360px to 640px wide"]
                           [:large-responsive "Flexible between 360px to 960px wide"]
                           [:small "Fixed 360px wide"]
                           [:regular "Fixed 640px wide"]]}]
    (sel-v/select-card options)))
