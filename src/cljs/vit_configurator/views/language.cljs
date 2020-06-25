(ns vit-configurator.views.language
  (:require [re-frame.core :as re-frame]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]
            [vit-configurator.views.select-view :as sel-v]))

(def languages
  [[:none "Not Specified"]
   [:en "English"]
   [:es "Spanish"]])

(defn customizer
  []
  (let [options {:title-text "What default language do you want the tool to use?"
                 :description-text "Leave set to 'Not Specified' if you want the tool to attempt to use the user's browser language setting to set itself. Otherwise you can override this with a pre-set default language. In both cases, the user can still use the language change option in the tool."
                 :subscription (re-frame/subscribe [::subs/language])
                 :event-kw ::events/set-language
                 :options languages}]
    (sel-v/select-card options)))
