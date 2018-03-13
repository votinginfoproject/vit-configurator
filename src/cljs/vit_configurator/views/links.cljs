(ns vit-configurator.views.links
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]
            [vit-configurator.views.shared :as shared]))

(def links-text
  "Change the text for the links that appear
in the Election Contact 'More Info' section.")

(defn link-field [link-kw placeholder component-state]
  (let [current-value @component-state
        max-length 45
        diff (- max-length (count current-value))
        border (shared/border-class diff)
        change-fn (shared/max-length-change-fn
                   max-length component-state [::events/set-link-text link-kw])]
    [:div.col
     [:input.w-100 (merge (when current-value
                            {:value current-value})
                          border
                          {:on-change change-fn
                           :type "text"
                           :key (str link-kw "-input")
                           :placeholder placeholder})]
     [shared/countdown diff]]))

(defn link-row [current-links row]
  [:div.row
   (map (partial link-field current-links) row)])

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
      [link-field :electionInfo "Election Information" election-info]
      [link-field :electionRegistration "Registration Information"
       election-registration]]
     [:div.row
      [link-field :electionRegistrationConfirmation
       "Registration Confirmation" confirmation]
      [link-field :absenteeVotingInfo "Absentee Voting Information"
       absentee-voting]]
     [:div.row
      [link-field :votingLocationFinder "Voting Location Finder"
       voting-location]
      [link-field :ballotInfo "Ballot Information" ballot-info]]
     [:div.row
      [link-field :electionRules "Election Rules" election-rules]]]))
