(ns vit-configurator.views
  (:require [clojure.string :as str]
            [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [vit-configurator.events :as events]
            [vit-configurator.subs :as subs]
            [vit-configurator.views.voter-info :as vi]
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
    :regular "width: 640px"
    "min-width: 320px, max-width: 640px"))

(defn code-snippet
  [{:keys [title logo language official-only links size voter-info]}]
  (str
   "<link rel=\"stylesheet\" type=\"text/css\" href=\"https://votinginfotool.org/css/compiled/site.css\"/>\n"
   "<script src=\"https://votinginfotool.org/js/compiled/app.js\"></script>\n"
   (str "<div id=\"_vit\" style=\"" (size-str size) "\"></div>\n")
   "<script>\n"
   "  var config = {\n"
   (when-not (str/blank? (:en title))
     (str "    \"title\": " (.stringify js/JSON (clj->js title)) ",\n"))
   (when-not (str/blank? (:en voter-info))
     (str "    \"voter-info\": " (.stringify js/JSON (clj->js voter-info)) ",\n"))
   "    \"logo\": " (.stringify js/JSON (clj->js logo)) ",\n"
   (when language
     (str "    \"language\": " (.stringify js/JSON (name language)) ",\n"))
   "    \"official-only\": " (.stringify js/JSON official-only)
   (when (seq links)
     (str ",\n    \"links\": " (.stringify js/JSON (clj->js links))))
   "\n  };\n"
   "  var loadVIT = function () {\n"
   "    if (typeof vit !== 'undefined'){\n"
   "      vit.core.init(\"_vit\", config);\n"
   "    } else {\n"
   "      setTimeout(loadVIT, 500);\n"
   "    };\n"
   "  };\n"
   "  loadVIT();\n"
   "</script>"))

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
  "mailto:?subject=VIP%20Embed%20Code&body=")

(def email-instructions
  "Put the following code into your HTML page to install the configured Voting Info Tool widget. It will need to be placed into the page at the place you want it to show up, ask your website maintainer for help if needed. Please be aware that some email programs like Outlook might wrap the links with a redirection link that serves the actual content from a Microsoft site. This is not recommended. Check after pasting in the code that it reads just like it does in this email, and that the script src link and stylesheet href both start with \"https://votinginfotool.org\".\n\n")

(defn mail-code
  [code]
  (let [encoded (.encodeURIComponent js/window (str email-instructions code))
        location (str mail-to-start encoded)]
    (set! (.-location js/window) location)))

(defn preview-size [size]
  (if (= size :small)
    {:width "400px"
     :height "500px"}
    {:width "700px"
     :height "500px"}))

(defn code
  []
  (let [clj-config @(re-frame/subscribe [::subs/clj-config])
        snippet   (code-snippet clj-config)]
    [open-card "Your custom embed code" :embed
     [:div
      [:p "The box below contains your custom embed code. You'll need to copy all the contents, and put them in the HTML page where you want the Voter Information Tool to show up. You can copy the contents automatically using the Copy to Clipboard button, and then paste them into your page directly, or send them on to your website maintainer. Alternatively, you can also use the E-mail Code button to bring up a new email in your default email program with the code already included, and just fill in the receiver address with either your own address or your website maintainer."]
      [:pre.p-2.border.border-black
       [:code {:id "_vit_code"} snippet]]
      [:textarea._vit_code_copy
       {:id "_vit_copy_code"
        :readOnly true
        :value snippet}]
      [:div
       [:button.btn.inline.left {:on-click code-to-clipboard} "Copy to Clipboard"]
       [:button.btn.inline {:on-click #(mail-code snippet)} "E-Mail Code"]]]]))

(defn main-panel []
  [:div.container
   [:div.row.justify-content-start {:style {"boxShadow" "0px 4px 2px grey"
                                            "marginBottom" "4px"}}
    [:div.col-1 [:img {:src "https://dashboard.votinginfoproject.org/assets/images/logo-vip.png"}]]
    [:div.col-6
     [:h6.text-uppercase "Voting Information Project"]
     [:h3 "VIT Configurator Dashboard"]]]
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
     [:div
        [:button.reload_preview.btn
         {:on-click #(re-frame/dispatch [::events/save-config-and-reload-preview])}
         "Save and Update Configuration"]]
     [card "Logo" :logo false [logo/customizer]]
     [card "Title" :title true [title/customizer]]
     [card "Voter Information Message" :voter-info true [vi/customizer]]
     [card "Language" :language true [language/customizer]]
     [card "Size" :size true [size/customizer]]
     [card "Official data use" :official-data true [official/customizer]]
     [card "Custom Election Info links" :links true [links/customizer]]
     [:div
        [:button.reload_preview.btn
         {:on-click #(re-frame/dispatch [::events/save-config-and-reload-preview])}
         "Save and Update Configuration"]]]
    (let [size   (:size @(re-frame/subscribe [::subs/clj-config]))]
      [:div.col-8.d-flex.flex-column
       [:div.container.d-flex.justify-content-center [:h4.pt-4 "Preview"]]
       [:p "You can preview your configuration changes in the sample tool below. Just make your changes in the left hand side and then click one of the \"Save and Update Configuration\" buttons to load the changes into the sample tool and code block below. " [:i "If you've made " [:strong "Voter Information Message"] " or " [:strong "Custom Link"] " changes, you should type " [:strong ":preview"] " into the address field and click on the Search button in order to see those in mocked results."] " Voter Information Messages appear after a search but before results, and custom link texts appear on the Contact Info tab."]
       [:iframe#live_preview {:src "live-preview.html"
                              :style (preview-size size)}]
       [code]])]])
