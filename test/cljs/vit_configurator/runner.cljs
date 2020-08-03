(ns vit-configurator.runner
    (:require [doo.runner :refer-macros [doo-all-tests doo-tests]]
              [vit-configurator.events-test]
              [vit-configurator.subs-test]))

(doo-all-tests #"^vit-configurator\..+-test$")
