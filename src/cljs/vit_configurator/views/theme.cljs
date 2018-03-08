(ns vit-configurator.views.theme
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]))

(def theme-text
  "Do you want to return only official state data
from the Google Civic Information API?")

(def themes
  [[:default "#273945" "#1390B7" "#043659 " "#9B0047"]
   [:theme-2 "#107BC4" "#1F5F99" "#043659" "#C1272D"]
   [:theme-3 "#FBB03B" "#107BC4" "#1F5F99" "#043659"]
   [:theme-4 "#893339" "#611417" "#107BC4" "#043659"]
   [:theme-5 "#5E6B72" "#2F3639" "#000000" "#CCCCCC"]
   [:theme-6 "#FFFFFF" "#000000" "#5E6B72" "#CCCCCC"]])

(defn theme-row
  [current-theme theme]
  (let [key (first theme)
        colors (rest theme)
        first-color (first colors)
        second-color (second colors)
        third-color (nth colors 2)
        fourth-color (last colors)]
    [:div.row.mb-2 (merge
                    {:key (name key)
                     :on-click #(re-frame/dispatch [::events/set-theme key])}
                    (if (= current-theme key)
                      {:style {:border "5px solid black"}}
                      {:style {:border "1px solid black"}}))
     [:div.col {:style {:height 20
                        :background-color first-color}}]
     [:div.col {:style {:background-color second-color}}]
     [:div.col {:style {:background-color third-color}}]
     [:div.col {:style {:background-color fourth-color}}]]))

(defn customizer
  []
  (let [theme @(re-frame/subscribe [::subs/theme])]
    [:div
     [:p.mb-1 theme-text]
     (map (partial theme-row theme) themes)]))
