(ns vit-configurator.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]
            [vit-configurator.views.language :as language]
            [vit-configurator.views.links :as links]
            [vit-configurator.views.logo :as logo]
            [vit-configurator.views.official :as official]
            [vit-configurator.views.size :as size]
            [vit-configurator.views.title :as title]))

(defn size-str [size]
  (case size
    :responsive "min-width: 320px, max-width: 640px"
    :small "width: 320px"
    :regular "width: 640px"))

(defn code-snippet
  [title logo language official links size]
  (str
   "<link rel=\"stylesheet\" type=\"text/css\" href=\"https://votinginfotool.votinginfoproject.org/css/compiled/site.css\"/>\n"
   "<script src=\"https://votinginfotool.votinginfoproject.org/js/compiled/app.js\"></script>\n"
   (str "<div id=\"_vit\" style=\"" (size-str size) "\"></div>\n")
   "<script>vit.core.init(\"_vit\",{\n"
   (when-not (= "Voting Information Tool" title)
     (str "\t\"title\": " (.stringify js/JSON (clj->js {"en" title})) ",\n"))
   "\t\"logo\": " (.stringify js/JSON (clj->js logo)) ",\n"
   (when-not (= :none language)
     (str "\t\"language\": " (.stringify js/JSON (name language)) ",\n"))
   "\t\"official-only\": " (.stringify js/JSON official)
   (when (and (seq links)
              (seq (:en links)))
     (str ",\n\t\"links\": " (.stringify js/JSON (clj->js links))))
   "\n});</script>"))

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

(defn code-to-clipboard
  "Code here cribbed from:
  https://www.w3schools.com/howto/howto_js_copy_clipboard.asp.
  The textarea is styled to be visible but placed off screen, because
  you can't copy from a hidden element."
  []
  (let [codebox (. js/document getElementById "_vit_copy_code")]
    (. codebox select)
    (. codebox setSelectionRange 0 99999)
    (. js/document execCommand "copy")
    (js/alert "Copied")))

(def mail-to-start
  "mailto:email@address.com?subject=VIP%20Embed%20Code&body=")

(defn mail-code
  [code]
  (let [encoded (.encodeURIComponent js/window code)
        location (str mail-to-start encoded)]
    (set! (.-location js/window) location)))

(defn preview-size [size]
  (if (= size :small)
    {:width "340px"
     :height "480px"}
    {:width "660px"
     :height "480px"}))

(defn main-panel []
  [:div.container
   [:div.row.justify-content-start {:style {"boxShadow" "0px 4px 2px grey"
                                            "marginBottom" "4px"}}
    [:div.col-1 [:img {:src "https://dashboard.votinginfoproject.org/assets/images/logo-vip.png"}]]
    [:div.col-6
     [:h6.text-uppercase "Voting Information Project"]
     [:h3 "Custom Widget Dashboard"]]]
   [:div.row.bg-light
    [:div.col-4.d-flex.flex-column
     [:p.pt-5.pl-5.pr-5
      [:strong "The Voting Information Tool"] " is an easily embeddable,"
      " mobile-optimized, white-label voting information tool that offers"
      " official voting information-such as polling place and ballot"
      " information-to anyone, using just a residential address. The tool"
      " can be embedded easily on any website and supports multiple languages."]
     [:p.pl-5.pr-5.pb-2
      "Using this widget you can easily customize the VIT by adding a"
      " state seal or your organization's logo, setting a default language, "
      " choosing whether to include unofficial data, or overriding the link "
      " titles for Election Official links."]
     [card "Logo" :logo false [logo/customizer]]
     [card "Title" :title true [title/customizer]]
     [card "Language" :language true [language/customizer]]
     [card "Size" :size true [size/customizer]]
     [card "Official data use" :official-data true [official/customizer]]
     [card "Custom Election Info links" :links true [links/customizer]]]
    (let [config @(re-frame/subscribe [::subs/config])
          size @(re-frame/subscribe [::subs/size])]
      [:div.col-8.d-flex.flex-column
       [:div.container.d-flex.justify-content-center [:h4.pt-4 "Preview"]]
       [:iframe#live_preview {:src "live-preview.html"
                              :style (preview-size size)}]
       [:div
        [:button#reload_preview.btn {:on-click #(re-frame/dispatch [::events/reload-preview])}
         "Reload Preview With Updated Configuration"]]
       (let [title     @(re-frame/subscribe [::subs/title])
             logo      @(re-frame/subscribe [::subs/logo])
             language  @(re-frame/subscribe [::subs/language])
             official  @(re-frame/subscribe [::subs/official-data-only])
             links     @(re-frame/subscribe [::subs/links])
             size      @(re-frame/subscribe [::subs/size])
             snippet   (code-snippet title logo language official links size)]
         [open-card "Your custom embed code" :embed
          [:div
           [:pre.p-2.border.border-black
            [:code {:id "_vit_code"} snippet]]
           [:textarea._vit_code_copy
            {:id "_vit_copy_code"
             :readOnly true
             :value snippet}]
           [:button {:on-click code-to-clipboard} "Copy to Clipboard"]
           [:button {:on-click #(mail-code snippet)} "E-Mail Code"]]])])]])
