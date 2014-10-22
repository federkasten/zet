(ns zet.zip
  (:import [java.io ByteArrayOutputStream]
           [java.util.zip Deflater Inflater]))

(defn deflate
  [#^bytes data]
  (let [dflt (doto (Deflater.)
                   (.setInput data)
                   (.finish))
        os (ByteArrayOutputStream.)
        buf (byte-array 1024)]
    (loop [finished? (.finished dflt)
           buf-size (.deflate dflt buf)]
      (when-not finished?
        (.write os buf 0 buf-size)
        (recur (.finished dflt)
               (.deflate dflt buf))))
    (.close os)
    (.end dflt)
    (.toByteArray os)))

(defn inflate
  [#^bytes data]
  (let [iflt (doto (Inflater.)
               (.setInput data))
        os (ByteArrayOutputStream.)
        buf (byte-array 1024)]
    (loop [finished? (.finished iflt)
           buf-size (.inflate iflt buf)]
      (when-not finished?
        (.write os buf 0 buf-size)
        (recur (.finished iflt)
               (.inflate iflt buf))))
    (.close os)
    (.end iflt)
    (.toByteArray os)))
