(ns vit-configurator.views.links
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]))

(def links-text
  "Change the text for the links that appear
in the Election Contact 'More Info' section.")

(defn link-field [link-kw placeholder component-value]
  (let [current-value @component-value
        max-length 45
        diff (- max-length (count current-value))
        border (when (neg? diff)
                 {:class "border-danger"})
        countdown-color (when (neg? diff)
                          {:class "text-danger"})
        change-fn (fn [evt]
                    (let [val (.. evt -target -value)]
                      (reset! component-value val)
                      (when (>= (- max-length (count val)) 0)
                        (re-frame/dispatch [::events/set-link-text
                                            link-kw val]))))]
    [:div.col
     [:input.w-100 (merge (when-let [val current-value]
                            {:value val})
                          (when border
                            border)
                          {:on-change change-fn
                           :type "text"
                           :key (str link-kw "-input")
                           :placeholder placeholder})]
     [:p.small countdown-color
      (if (>= diff 0)
        (str "(" diff " characters remaining)")
        (str "(" (Math/abs diff) " characters over)"))]]))

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
