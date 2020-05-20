(ns vit-configurator.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [vit-configurator.subs :as subs]
            [vit-configurator.views.language :as language]
            [vit-configurator.views.links :as links]
            [vit-configurator.views.logo :as logo]
            [vit-configurator.views.official :as official]))

;; TODO Make the urls environment specific
(defn code-snippet []
  (let [logo @(re-frame/subscribe [::subs/logo])
        language @(re-frame/subscribe [::subs/language])
        official @(re-frame/subscribe [::subs/official-data-only])
        links @(re-frame/subscribe [::subs/links])]
    [:pre.p-2.border.border-black
     [:code
      "<link rel=\"stylesheet\" type=\"text/css\" href=\"https://votinginfotool.votinginfoproject.org/css/compiled/site.css\"/>\n"
      "<script src=\"https://votinginfotool.votinginfoproject.org/js/compiled/app.js\"></script>\n"
      "<div id=\"_vit\" class=\"app-container\"></div>\n"
      "<script>gttp2.core.init(\"_vit\",{\n"
      "\t\"logo\": " (.stringify js/JSON (clj->js logo)) ",\n"
      "\t\"language\": " (.stringify js/JSON (name language)) ",\n"
      "\t\"official-only\": " (.stringify js/JSON official)
      (when (seq links)
        (str ",\n\t\"links\": " (.stringify js/JSON (clj->js {"en" links}))))
      "\n});</script>"]]))

(defn open-card
  [title-str key-val content]
  [:div.card.ml-3.mr-3.mb-2.pb-0 {:key (gensym key-val)
                                  :style {"boxShadow" "4px 4px grey"}}
   [:div.card-body.pb-0
    [:p.card-title.text-secondary.font-weight-bold
     [:span title-str]]
    [:div.gttp-card-text-wrapper
     content]]])

(defn card
  [title-str key-val initially-closed? content]
  (let [card-closed? (reagent/atom initially-closed?)]
    ;;Note: we only care about initially-closed? on the initial outer call,
    ;;so we ignore the parameter in the render function below /shrug
    (fn [title-str key-val _ content]
      [:div.card.ml-1.mr-1.mb-2.pb-0 {:key (gensym key-val)
                                      :style {"boxShadow" "4px 4px grey"}}
       [:div.card-body.pb-0
        [:p.card-title.text-secondary.font-weight-bold
         [:span title-str]
         [:i.material-icons.text-medium-gray.toggle-card
          {:on-click #(swap! card-closed? not)}
          (if @card-closed? "keyboard_arrow_down" "keyboard_arrow_up")]]
        [:div.gttp-card-text-wrapper
         {:class (if @card-closed? "d-none" "d-block")}
         content]]])))


(defn main-panel []
  [:div.container
   [:div.row.justify-content-start {:style {"boxShadow" "0px 4px 2px grey"
                                            "marginBottom" "4px"}}
    [:div.col-1 [:img {:src "https://dashboard.votinginfoproject.org/assets/images/logo-vip.png"}]]
    [:div.col-6
     [:h6.text-uppercase "Voting Information Project"]
     [:h3 "Custom Widget Dashboard"]]]
   [:div.row.bg-light
    [:div.col-5.d-flex.flex-column
     [:p.pt-5.pl-5.pr-5
      [:strong "The Voting Information Tool"] "is an easily embeddable,"
      " mobile-optimized, white-label voting information tool that offers"
      " official voting information-such as polling place and ballot"
      " information-to anyone, using just a residential address. The tool"
      " can be embedded easily on any website and supports multiple languages."]
     [:p.pl-5.pr-5.pb-2
      "Using this widget you can easily customize the VIT by adding a"
      " state seal or your organization's logo, including a custom alert,"
      " such as \"Don't forget to vote on Election Day!\", or by modifying"
      " the colors."]
     [card "Logo" :logo false [logo/customizer]]
     [card "Language" :language true [language/customizer]]
     [card "Official data use" :official-data true [official/customizer]]
     [card "Custom Election Info links" :links true [links/customizer]]]
    [:div.col-7.d-flex.flex-column
     [:div.container.d-flex.justify-content-center [:h4.pt-4 "Preview"]]
     [:div.container.d-flex.justify-content-center.pb-3
      [:img {:src "./images/responsive-mockup.jpeg"}]]
     [open-card "Your custom embed code" :embed
      [code-snippet]]]]])
