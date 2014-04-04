(ns bløggr.core
  (:require [bløggr.common :refer :all]
            [bløggr.posts :refer :all]
            [bløggr.index :refer :all]
            [bløggr.assets :refer :all]
            [stasis.core :as stasis]
            [optimus.prime :as optimus]
            [optimus.assets :as assets]
            [optimus.optimizations :as optimizations]
            [optimus.strategies :as strategies]))

(defn get-page-sources []
  (let [posts (get-posts)]
    (hash-map :css (get-css)
              :posts (reduce merge (map filename-body-map posts))
              :index {"/index.html" (->> posts
                                        (posts-by-date)
                                        (get-index))})))

(def ring (-> (get-page-sources)
              (stasis/merge-page-sources)
              (stasis/serve-pages)
              (optimus/wrap get-assets optimizations/none strategies/serve-live-assets)))

