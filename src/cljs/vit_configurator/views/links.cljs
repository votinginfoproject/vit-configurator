(ns vit-configurator.views.links
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]
            [vit-configurator.views.shared :as shared]))

(def links-text
  "Change the text for the links that appear
in the Election Contact 'More Info' section.")

(defn link-field [link-kw default-text component-state]
  (let [current-value @component-state
        max-length 45
        diff (- max-length (count current-value))
        border (shared/border-class diff)
        change-fn (shared/max-length-change-fn
                   max-length component-state [::events/set-link-text link-kw])]
    [:div.col
     [:div default-text]
     [:input.w-100 (merge (when current-value
                            {:value current-value})
                          border
                          {:on-change change-fn
                           :type "text"
                           :key (str link-kw "-input")
                           :placeholder default-text})]
     [shared/countdown diff]]))

(defn customizer
  []
  (let [election-info (reagent/atom nil)
        election-registration (reagent/atom nil)
        confirmation (reagent/atom nil)
        absentee-voting (reagent/atom nil)
        voting-location (reagent/atom nil)
        ballot-info (reagent/atom nil)
        election-rules (reagent/atom nil)]
    [:div
     [:p links-text]
     [:div.row
      [link-field :electionInfoUrl "Election Information" election-info]]
     [:div.row
      [link-field :electionRegistrationUrl "Registration Information"
       election-registration]]
     [:div.row
      [link-field :electionRegistrationConfirmationUrl
       "Registration Confirmation" confirmation]]
     [:div.row
      [link-field :absenteeVotingInfoUrl "Absentee Voting Information"
       absentee-voting]]
     [:div.row
      [link-field :votingLocationFinderUrl "Voting Location Finder"
       voting-location]]
     [:div.row
      [link-field :ballotInfoUrl "Ballot Information" ballot-info]]
     [:div.row
      [link-field :electionRulesUrl "Election Rules" election-rules]]]))
