(ns vit-configurator.views.theme
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]))

(def config
  {:default {"home-header-background" "#273945"
             "home-header-text" "#FFFFFF"}
   :theme-2 {"home-background" "#107BC4"
             "home-text" "#FFFFFF"
             "home-header-background" "#107BC4"
             "home-header-text" "#FFFFFF"
             "callout-background" "#C1272D"
             "callout-checkmark-color" "#043659"
             "callout-text" "#FFFFFF"
             "about-background" "#1F5F99"
             "about-text" "#FFFFFF"
             "button-background" "#043659"
             "button-text" "#FFFFFF"
             "navigation-text" "#C1272D"
             "header-text" "#107BC4"
             "info-background" "#043659"}
   :theme-3 {"home-background" "#107BC4"
             "home-text" "#FFFFFF"
             "home-header-background" "#107BC4"
             "home-header-text" "#FFFFFF"
             "callout-background" "#043659"
             "callout-checkmark-color" "#FBB03B"
             "callout-text" "#FFFFFF"
             "about-background" "#1F5F99"
             "about-text" "#FFFFFF"
             "button-background" "#FBB03B"
             "button-text" "#043659"
             "navigation-text" "#107BC4"
             "header-text" "#107BC4"
             "info-background" "#107BC4"}
   :theme-4 {"home-background" "#893339"
             "home-text" "#FFFFFF"
             "home-header-background" "#893339"
             "home-header-text" "#FFFFFF"
             "callout-background" "#043659"
             "callout-checkmark-color" "#107BC4"
             "callout-text" "#FFFFFF"
             "about-background" "#611417"
             "about-text" "#FFFFFF"
             "button-background" "#107BC4"
             "button-text" "#FFFFFF"
             "navigation-text" "#043659"
             "header-text" "#893339"
             "info-background" "#893339"}
   :theme-5 {"home-background" "#2F3639"
             "home-text" "#FFFFFF"
             "home-header-background" "#2F3639"
             "home-header-text" "#FFFFFF"
             "callout-background" "#5E6B72"
             "callout-checkmark-color" "#FFFFFF"
             "callout-text" "#FFFFFF"
             "about-background" "#5E6B72"
             "about-text" "#FFFFFF"
             "button-background" "#CCCCCC"
             "button-text" "#2F3639"
             "navigation-text" "#5E6B72"
             "header-text" "#5E6B72"
             "info-background" "#2F3639"}
   :theme-6 {"home-background" "#FFFFFF"
             "home-text" "#000000"
             "home-header-background" "#FFFFFF"
             "home-header-text" "#000000"
             "callout-background" "#5E6B72"
             "callout-checkmark-color" "#FFFFFF"
             "callout-text" "#FFFFFF"
             "about-background" "#2F3639"
             "about-text" "#FFFFFF"
             "button-background" "#5E6B72"
             "button-text" "#FFFFFF"
             "navigation-text" "#2F3639"
             "header-text" "#5E6B72"
             "info-background" "#5E6B72"}})

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
                      {:class "selected-border"}
                      {:class "border"}))
     [:div.col.h-25-static {:style {:background-color first-color}}]
     [:div.col {:style {:background-color second-color}}]
     [:div.col {:style {:background-color third-color}}]
     [:div.col {:style {:background-color fourth-color}}]]))

(defn customizer
  []
  (let [theme @(re-frame/subscribe [::subs/theme])]
    [:div
     [:p.mb-1 theme-text]
     (map (partial theme-row theme) themes)]))
