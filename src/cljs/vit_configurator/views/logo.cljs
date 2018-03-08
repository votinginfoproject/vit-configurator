(ns vit-configurator.views.logo
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]))

(def seals
  [[:al "Alabama" "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f7/Seal_of_Alabama.svg/240px-Seal_of_Alabama.svg.png"]
   [:ak "Alaska" "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2b/Alaska-StateSeal.svg/238px-Alaska-StateSeal.svg.png"]
   [:az "Arizona" "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Arizona-StateSeal.svg/239px-Arizona-StateSeal.svg.png"]
   [:ar "Arkansas" "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a4/Seal_of_Arkansas.svg/240px-Seal_of_Arkansas.svg.png"]
   [:ca "California" "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0f/Seal_of_California.svg/240px-Seal_of_California.svg.png"]
   [:co "Colorado" "https://upload.wikimedia.org/wikipedia/commons/thumb/0/00/Seal_of_Colorado.svg/240px-Seal_of_Colorado.svg.png"]
   [:ct "Connecticut" "https://upload.wikimedia.org/wikipedia/commons/thumb/4/48/Seal_of_Connecticut.svg/187px-Seal_of_Connecticut.svg.png"]
   [:de "Delaware" "https://upload.wikimedia.org/wikipedia/commons/thumb/3/35/Seal_of_Delaware.svg/240px-Seal_of_Delaware.svg.png"]
   [:fl "Florida" "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2b/Seal_of_Florida.svg/240px-Seal_of_Florida.svg.png"]
   [:ga "Georgia" "https://upload.wikimedia.org/wikipedia/commons/thumb/7/79/Seal_of_Georgia.svg/240px-Seal_of_Georgia.svg.png"]
   [:hi "Hawaii" "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Seal_of_the_State_of_Hawaii.svg/240px-Seal_of_the_State_of_Hawaii.svg.png"]
   [:id "Idaho" "https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/Seal_of_Idaho.svg/237px-Seal_of_Idaho.svg.png"]
   [:il "Illinois" "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e7/Seal_of_Illinois.svg/240px-Seal_of_Illinois.svg.png"]
   [:in "Indiana" "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c4/Indiana-StateSeal.svg/240px-Indiana-StateSeal.svg.png"]
   [:io "Iowa" "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5a/Iowa-StateSeal.svg/238px-Iowa-StateSeal.svg.png"]
   [:ka "Kansas" "https://upload.wikimedia.org/wikipedia/commons/thumb/4/45/Seal_of_Kansas.svg/240px-Seal_of_Kansas.svg.png"]
   [:kt "Kentucky" "https://upload.wikimedia.org/wikipedia/commons/thumb/3/35/Seal_of_Kentucky.svg/240px-Seal_of_Kentucky.svg.png"]
   [:la "Louisiana" "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Seal_of_Louisiana.svg/240px-Seal_of_Louisiana.svg.png"]
   [:me "Maine" "https://upload.wikimedia.org/wikipedia/commons/thumb/6/63/Seal_of_Maine.svg/240px-Seal_of_Maine.svg.png"]
   [:md "Maryland" "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1f/Seal_of_Maryland_%28obverse%29.png/240px-Seal_of_Maryland_%28obverse%29.png"]
   [:ma "Massachusetts" "https://upload.wikimedia.org/wikipedia/commons/thumb/8/82/Seal_of_Massachusetts.svg/240px-Seal_of_Massachusetts.svg.png"]
   [:mi "Michigan" "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Seal_of_Michigan.svg/240px-Seal_of_Michigan.svg.png"]
   [:mn "Minnesota" "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a1/Minnesota_state_coat_of_arms_%28illustrated%2C_1876%29.jpg/197px-Minnesota_state_coat_of_arms_%28illustrated%2C_1876%29.jpg"]
   [:ms "Mississippi" "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Seal_of_Mississippi_%282014-present%29.svg/240px-Seal_of_Mississippi_%282014-present%29.svg.png"]
   [:mo "Missouri" "https://upload.wikimedia.org/wikipedia/commons/thumb/d/de/Seal_of_Missouri.svg/240px-Seal_of_Missouri.svg.png"]
   [:mt "Montana" "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ed/Montana-StateSeal.svg/239px-Montana-StateSeal.svg.png"]
   [:ne "Nebraska" "https://upload.wikimedia.org/wikipedia/commons/thumb/7/73/Seal_of_Nebraska.svg/238px-Seal_of_Nebraska.svg.png"]
   [:nv "Nevada" "https://upload.wikimedia.org/wikipedia/commons/thumb/7/77/Nevada-StateSeal.svg/240px-Nevada-StateSeal.svg.png"]
   [:nh "New Hampshire" "https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/Seal_of_New_Hampshire.svg/240px-Seal_of_New_Hampshire.svg.png"]
   [:nj "New Jersey" "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8d/Seal_of_New_Jersey.svg/240px-Seal_of_New_Jersey.svg.png"]
   [:nm "New Mexico" "https://upload.wikimedia.org/wikipedia/commons/thumb/9/92/Seal_of_New_Mexico.svg/238px-Seal_of_New_Mexico.svg.png"]
   [:ny "New York" "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Seal_of_New_York.svg/240px-Seal_of_New_York.svg.png"]
   [:nc "North Carolina" "https://upload.wikimedia.org/wikipedia/commons/thumb/7/72/Seal_of_North_Carolina.svg/240px-Seal_of_North_Carolina.svg.png"]
   [:nd "North Dakota" "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e7/NorthDakota-StateSeal.svg/240px-NorthDakota-StateSeal.svg.png"]
   [:oh "Ohio" "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Seal_of_Ohio.svg/240px-Seal_of_Ohio.svg.png"]
   [:ok "Oklahoma" "https://upload.wikimedia.org/wikipedia/commons/thumb/3/39/Seal_of_Oklahoma.svg/238px-Seal_of_Oklahoma.svg.png"]
   [:or "Oregon" "https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Seal_of_Oregon.svg/240px-Seal_of_Oregon.svg.png"]
   [:pa "Pennsylvania" "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Seal_of_Pennsylvania.svg/240px-Seal_of_Pennsylvania.svg.png"]
   [:ri "Rhode Island" "https://upload.wikimedia.org/wikipedia/commons/thumb/7/76/Seal_of_Rhode_Island.svg/240px-Seal_of_Rhode_Island.svg.png"]
   [:sc "South Carolina" "https://upload.wikimedia.org/wikipedia/commons/thumb/8/80/Seal_of_South_Carolina.svg/238px-Seal_of_South_Carolina.svg.png"]
   [:sd "South Dakota" "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bb/SouthDakota-StateSeal.svg/240px-SouthDakota-StateSeal.svg.png"]
   [:tn "Tennessee" "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3c/Seal_of_Tennessee.svg/239px-Seal_of_Tennessee.svg.png"]
   [:tx "Texas" "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cb/Seal_of_Texas.svg/240px-Seal_of_Texas.svg.png"]
   [:ut "Utah" "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Seal_of_Utah.svg/240px-Seal_of_Utah.svg.png"]
   [:vt "Vermont" "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5b/Vermont_state_seal.svg/240px-Vermont_state_seal.svg.png"]
   [:va "Virginia" "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Seal_of_Virginia.svg/240px-Seal_of_Virginia.svg.png"]
   [:wa "Washington" "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3d/Seal_of_Washington.svg/240px-Seal_of_Washington.svg.png"]
   [:wv "West Virginia" "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1c/Seal_of_West_Virginia.svg/240px-Seal_of_West_Virginia.svg.png"]
   [:wi "Wisconsin" "https://upload.wikimedia.org/wikipedia/commons/thumb/3/31/Seal_of_Wisconsin.svg/240px-Seal_of_Wisconsin.svg.png"]
   [:wy "Wyoming" "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e4/Seal_of_Wyoming.svg/240px-Seal_of_Wyoming.svg.png"]])

(def state-abbreviations
  (->> seals (map first) set))

(defn select-option
  [[abbreviation state-name _]]
  [:option {:value abbreviation
            :key (name abbreviation)} state-name])

(defn customizer
  "The component local state (custom-logo-value) keeps track of the dirty
   state of the custom URL entry, and on blur that gets emitted as an event.
   A subscription to ::subs/logo allows it to react to changes in the widget."
  []
  (let [custom-logo-value (reagent/atom "")
        state-seal-value (reagent/atom :al)]
    (fn []
      (let [{:keys [type value]} @(re-frame/subscribe [::subs/logo])]
        [:div.container.justify-space-between
         [:div.row.mb-4
          [:div.col
           [:h6.text-uppercase "Default"]
           [:div.h-75 {:class (if (= :default type) "border-active" "border")
                       :on-click #(re-frame/dispatch [::events/set-logo :default])}
            [:div.pt-2
             [:img.mx-auto.d-block
              {:src "https://dashboard.votinginfoproject.org/assets/images/logo-vip.png"
               :width 50 :height 49}]]]]
          [:div.col
           [:h6.text-uppercase "None"]
           [:div.h-75 {:class (if (= :none type) "border-active" "border")
                       :on-click #(re-frame/dispatch [::events/set-logo :none])}
            [:div.pt-2
             [:img.mx-auto.d-block {:width 50 :height 50
                                    :src "images/circle-slash.png"}]]]]]
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
                            (when (= :state-seal type)
                              (re-frame/dispatch [::events/set-logo :state-seal
                                                  abbrev]))))}
            (map select-option seals)]
           [:div.h-50 {:class (if (= :state-seal type)
                                "border-active" "border")
                       :on-click #(re-frame/dispatch [::events/set-logo :state-seal
                                                      @state-seal-value])}
            [:div.pt-2.pb-3
             [:img.mx-auto.d-block
              {:width 50 :height 50
               :src (->> seals
                         (filter #(= @state-seal-value (first %)))
                         first
                         last)}]]]]]
         [:div.row.mx-1.mb-2.d-flex.flex-column {:style {:height 150}}
          [:h6.text-uppercase "Url"]
          [:input {:value @custom-logo-value
                   :type :text
                   :placeholder "https://your.server/img/logo.png"
                   :on-change (fn [evt]
                                (let [val (.. evt -target -value)]
                                  (reset! custom-logo-value val)
                                  (when (= :custom type)
                                    (re-frame/dispatch
                                     [::events/set-logo :custom val]))))}]
          [:div {:class (if (= :custom type)
                          "border-active" "border")
                 :on-click #(re-frame/dispatch
                             [::events/set-logo :custom @custom-logo-value])}
           [:div.pt-2.pb-2
            [:img.mx-auto.d-block {:src @custom-logo-value :width "50" :height "50"}]]]]]))))
