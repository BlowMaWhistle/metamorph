; SPDX-FileCopyrightText: 2022 Alliander N.V.
;
; SPDX-License-Identifier: Apache-2.0

(ns metamorph.core-test
  (:require [cli-matic.core :refer [run-cmd*]]
    [kaocha.repl :as k]
    [cheshire.core :as json]
    [tick.core :as t]
    [tick.locale-en-us]
    [deercreeklabs.lancaster :as l]
    [clojure.java.io :as io]
    [clojure.test :refer [deftest is testing]]
    [metamorph.core :as SUT])
  )

(defn- dts []
  (t/format "YYYYMMddHHmm" (t/zoned-date-time)))

; (defn- read-avro-schema [path]
;   (let [parser (Schema$Parser.)]
;     (.parse parser (io/file path))))
; ; (json/parse-stream (io/reader path)))

(deftest avro-schema-generation
  (testing "Generation of Avro schema [CLI]"
    (testing "from DX Profile"
      (testing "to JSON file [default location]"
        (let [fname "./avro.json"]
          (is (=
                (run-cmd* SUT/command-spec ["--dx-profile" "dev-resources/example_profile" "avro"])
                {:retval 0, :status :OK, :help nil, :subcmd [], :stderr []})
            )
          (is (=
                (slurp "./test/metamorph/avro_success.json")
                (slurp fname)))
          (io/delete-file fname)))
      
      (testing "to JSON file [specified location]"
        (let [fname (str "./" (dts) "-avro.json")]
          (is (=
                (run-cmd* SUT/command-spec ["--dx-profile" "dev-resources/example_profile" "avro" "--output" fname])
                {:retval 0, :status :OK, :help nil, :subcmd [], :stderr []})
            )
          (is (=
                (slurp "./test/metamorph/avro_success.json")
                (slurp fname)))
          (io/delete-file fname))))))

(comment
  "
  1. Change PWD before running; or generate name using DTS so it cannot possibly exist
  2. Delete files afterwards

  "



  (k/run-all))
