(ns vit-configurator.events-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [vit-configurator.events :as events]))

(deftest add-language-code-test
  (testing "value present"
    (let [m {:foo "foo"}]
      (is (= {:foo {:en "foo"}}
             (events/add-language-code m :foo)))))
  (testing "value missing"
    (let [m {:foo "foo"}]
      (is (= {:foo "foo"}
             (events/add-language-code m :bar))))))

(deftest save-config-test
  (testing "emtpy config"
    (let [ctx (events/save-config-and-reload-preview {:db {:config {}}})]
      (is (= {}
             (get-in ctx [:db :config])))))
  (testing "all values"
    (let [ctx (events/save-config-and-reload-preview
               {:db {:logo {:type "default"}
                     :language :en
                     :size :responsive
                     :title "My Cool Title"
                     :links {:electionInfoUrl "Cool Election Info"}
                     :voter-info "Important Voter Message"}})]
      (is (= {:logo {:type "default"}
              :language :en
              :size :responsive
              :title {:en "My Cool Title"}
              :links {:en {:electionInfoUrl "Cool Election Info"}}
              :voter-info {:en "Important Voter Message"}}
             (get-in ctx [:db :config]))))))
