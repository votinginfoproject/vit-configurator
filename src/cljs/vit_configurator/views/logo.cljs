(ns vit-configurator.views.logo
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]))

(def seals
  [[:al "Alabama"]
   [:ak "Alaska"]
   [:az "Arizona"]
   [:ar "Arkansas"]
   [:ca "California"]
   [:co "Colorado"]
   [:ct "Connecticut"]
   [:de "Delaware"]
   [:dc "District of Columbia"]
   [:fl "Florida"]
   [:ga "Georgia"]
   [:hi "Hawaii"]
   [:id "Idaho"]
   [:il "Illinois"]
   [:in "Indiana"]
   [:ia "Iowa"]
   [:ks "Kansas"]
   [:ky "Kentucky"]
   [:la "Louisiana"]
   [:me "Maine"]
   [:md "Maryland"]
   [:ma "Massachusetts"]
   [:mi "Michigan"]
   [:mn "Minnesota"]
   [:ms "Mississippi"]
   [:mo "Missouri"]
   [:mt "Montana"]
   [:ne "Nebraska"]
   [:nv "Nevada"]
   [:nh "New Hampshire"]
   [:nj "New Jersey"]
   [:nm "New Mexico"]
   [:ny "New York"]
   [:nc "North Carolina"]
   [:nd "North Dakota"]
   [:oh "Ohio"]
   [:ok "Oklahoma"]
   [:or "Oregon"]
   [:pa "Pennsylvania"]
   [:ri "Rhode Island"]
   [:sc "South Carolina"]
   [:sd "South Dakota"]
   [:tn "Tennessee"]
   [:tx "Texas"]
   [:ut "Utah"]
   [:vt "Vermont"]
   [:va "Virginia"]
   [:wa "Washington"]
   [:wv "West Virginia"]
   [:wi "Wisconsin"]
   [:wy "Wyoming"]])

(def state-abbreviations
  (->> seals (map first) set))

(defn select-option
  [[abbreviation state-name]]
  [:option {:value abbreviation
            :key (name abbreviation)} state-name])

(defn state-seal-logo-from-abbreviation
  [abbreviation]
  (str "https://vit-logos.votinginfoproject.org/seals/" (name abbreviation) ".png"))

(defn state-seal
  [type state-seal-value]
  [:div.row.mb-3
   [:div.col
    [:h6.text-uppercase "State Seal"]
    [:select
     {:value @state-seal-value
      :on-change (fn [selection]
                   (let [abbrev (-> selection
                                    (.. -target -value)
                                    keyword)]
                     (reset! state-seal-value abbrev)
                     (re-frame/dispatch [::events/set-logo :state-seal
                                         abbrev])))}
     (map select-option seals)]
    [:div.h-50 {:class (if (= :state-seal type)
                         "border-active" "border")
                :on-click #(re-frame/dispatch [::events/set-logo :state-seal
                                               @state-seal-value])}
     [:div.pt-2.pb-3
      [:img.mx-auto.d-block.squared-logo
       {:src (state-seal-logo-from-abbreviation @state-seal-value)}]]]]])

(defn default-logo
  [type]
  [:div.col
   [:h6.text-uppercase "Default"]
   [:div.h-75 {:class (if (= :default type) "border-active" "border")
               :on-click #(re-frame/dispatch [::events/set-logo :default])}
    [:div.pt-2
     [:img.mx-auto.d-block.vip-logo
      {:src "https://dashboard.votinginfoproject.org/assets/images/logo-vip.png"}]]]])

(defn no-logo
  [type]
  [:div.col
   [:h6.text-uppercase "None"]
   [:div.h-75 {:class (if (= :none type) "border-active" "border")
               :on-click #(re-frame/dispatch [::events/set-logo :none])}
    [:div.pt-2
     [:img.mx-auto.d-block.squared-logo
      {:src "images/circle-slash.png"}]]]])

(defn custom-logo
  [type custom-logo-value]
  [:div.row.mx-1.mb-2.d-flex.flex-column.h-150-static
   [:h6.text-uppercase "Url"]
   [:input {:value @custom-logo-value
            :type :text
            :placeholder "https://your.server/img/logo.png"
            :on-change (fn [evt]
                         (let [val (.. evt -target -value)]
                           (reset! custom-logo-value val)
                           (re-frame/dispatch
                            [::events/set-logo :custom val])))}]
   [:div {:class (if (= :custom type)
                   "border-active" "border")
          :on-click #(re-frame/dispatch
                      [::events/set-logo :custom @custom-logo-value])}
    [:div.pt-2.pb-2.h-50-static
     [:img.mx-auto.d-block.squared-logo
      {:src @custom-logo-value}]]]])

(defn customizer
  "The component local state (custom-logo-value) keeps track of the dirty
   state of the custom URL entry, and on blur that gets emitted as an event.
   A subscription to ::subs/logo allows it to react to changes in the widget."
  []
  (let [custom-logo-value (reagent/atom "")
        state-seal-value (reagent/atom :al)]
    (fn []
      (let [{:keys [type]} @(re-frame/subscribe [::subs/logo])]
        [:div.container.justify-space-between
         [:div.row.mb-4
          [default-logo type]
          [no-logo type]]
         [state-seal type state-seal-value]
         [custom-logo type custom-logo-value]]))))
