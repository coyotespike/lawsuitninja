(ns lawsuitninja.emails
  (:require
   [postal.core :refer [send-message]]
   [clj-time.core :as t]
   [clj-time.local :as l]
   [clj-time.format :as f]
   [clojure.java.io :as io]
   [net.cgrand.enlive-html :refer [deftemplate 
                                   set-attr 
                                   html-resource
                                   defsnippet
                                   substitute
                                   at
                                   emit*]])

  (import java.io.StringReader))



;;;; In my other project, Expunct, I discovered multimethods and reformed this
;;;; horrible file. It was always intended to be temporary, but was not worth
;;;; refactoring when we changed focus.

(def host-email "User email for gmail hosting" 
  "example@lawsuitninja.com")
(def pass "my gmail password"
  "example")
(def our-emails "The user's messages will be sent to these addresses."
  ["example@gmail.com" "example.edu"])

(def conn "The configuration for Gmail's hosting."
  {:host "smtp.gmail.com"
   :ssl true
   :user host-email
   :pass pass})

(def full-day "Uses the clj-time library to get the day of the week."
  (f/formatter "EEEE"))
(def my-time-now "Gets a normal-looking local time"
  (l/format-local-time (l/local-now) :hour-minute))
(def my-today "Actually turns the 'EEEE' into a real word."
  (f/unparse full-day (l/local-now)))
(def tomorrow "Does the same for tomorrow."
  (f/unparse full-day (t/plus (l/local-now) (t/hours 24))))

(def plain-message "Sent if the user email can't support HTML."
  (str "Hey, Lawsuit Ninja got your message! We'll get right to work adding useful information. We got your message at " my-time-now " on " my-today ", so we'll get back to you by " my-time-now ", " tomorrow ". If we haven't replied to your message by then, just let us know to get your $5!"))

(def replacement-text "HTML with the right times and days of the week."
  [{:type :element, :attrs {:id "onTheClock", :style "margin: 1em 0;padding: 0;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;color: #606060;font-family: Helvetica;font-size: 15px;line-height: 150%;text-align: left;"}, :tag :p, :content [(str "We got your message at " my-time-now " on " my-today ", so we'll get back to you by " my-time-now ", " tomorrow ". If we haven't replied to your message by then, use the Pay Up button to get your $5.")]}])
(def e-parsed "Turns the HTML email template into Enlive code."
  (html-resource "templates/email.html"))

(def replaced "Replaces the default HTML with the better HTML."
  (at e-parsed [:#whatWhat] (substitute replacement-text)))
(def finished "Turns the finished Enlive code back into real HTML."
  (apply str (emit* replaced)))


(defn send-email 
  "Lets the user know we are working on their request, and sends us the user
  request."
  [body]
  (let [user-email (:email body)
        user-message (:user-message body)]
    ;; Email to user
    (send-message conn
                  {:from "lawsuitninja@gmail.com"
                   :to user-email
                   :subject "Thanks for your message!"
                   :body [:alternative
                          {:type "text/plain"
                           :content plain-message}
                          {:type "text/html"
                           :content finished}]})
    ;; Email to us
    (send-message conn {:from user-email
                        :to our-emails
                        :subject "Your users love you!"
                        :body user-message})))

(def pay-message "Sent if the user email can't support HTML."
  (str "Yikes! You'll bankrupt us! Let us check out your request for information, and if we didn't respond, we'll mail you a little Abe Lincoln to be your friend. Talk to you soon!"))

(def pay-replacement-text "HTML with the right times and days of the week."
  [{:type :element, :attrs {:id "onTheClock", :style "margin: 1em 0;padding: 0;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;color: #606060;font-family: Helvetica;font-size: 15px;line-height: 150%;text-align: left;"}, :tag :p, :content [  (str "Yikes! You'll bankrupt us! Let us check out your request for information, and if we didn't respond, we'll mail you a little Abe Lincoln to be your friend. Talk to you soon!")]}])
(def pay-parsed "Turns the HTML email template into Enlive code."
  (html-resource "templates/pay-email.html"))

(def pay-replaced "Replaces the default HTML with the better HTML."
  (at pay-parsed [:#whatWhat] (substitute pay-replacement-text)))
(def pay-finished "Turns the finished Enlive code back into real HTML."
  (apply str (emit* pay-replaced)))


(defn pay-email
  "Lets the user know we have their request for money, and will pay soon."
  [body]
  (let [user-address (:user-address body)
        user-message (:user-message body)
        user-name (:name body)
        user-email (:user-email body)]
    ;; Email to user
    (send-message conn
                  {:from "admin@lawsuitninja.com"
                   :to user-email
                   :subject "Money money money!"
                   :body [:alternative
                          {:type "text/plain"
                           :content pay-message}
                          {:type "text/html"
                           :content pay-finished}]})
    ;; Email to us
    (send-message conn {:from user-email
                        :to our-emails
                        :subject "Someone wants money!!"
                        :body (str user-name " says: " user-message
                                   " and they live at: " user-address)})))





(def welcome-message "Sent if the user email can't support HTML."
  (str "Welcome to Lawsuit Ninja! We're excited to have you with us, and we look forward to responding to your questions and requests for information. In the near future, we'll open a forum where you can talk with other people working on various legal issues. See you around!"))

(def welcome-replacement-text "HTML with the right times and days of the week."
  [{:type :element, :attrs {:id "onTheClock", :style "margin: 1em 0;padding: 0;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;color: #606060;font-family: Helvetica;font-size: 15px;line-height: 150%;text-align: left;"}, :tag :p, :content [  (str "Welcome to Lawsuit Ninja! We're excited to have you with us, and we look forward to responding to your questions and requests for information. In the near future, we'll open a forum where you can talk with other people working on various legal issue. See you around!")]}])
(def welcome-parsed "Turns the HTML email template into Enlive code."
  (html-resource "templates/welcome-email.html"))

(def welcome-replaced "Replaces the default HTML with the better HTML."
  (at welcome-parsed [:#whatWhat] (substitute welcome-replacement-text)))
(def welcome-finished "Turns the finished Enlive code back into real HTML."
  (apply str (emit* welcome-replaced)))


(defn welcome-email
  "Welcomes the user to Lawsuit Ninja."
  [user-email firstname]
  (let [nice-subject (str firstname ", a journey of a thousand miles begins with a single step")
        plain-subject "A journey of a thousand miles begins with a single step"]
    ;; Email to user
    (send-message conn
                  {:from "admin@lawsuitninja.com"
                   :to user-email
                   :subject (if (some? firstname) nice-subject plain-subject)
                   :body [:alternative
                          {:type "text/plain"
                           :content welcome-message}
                          {:type "text/html"
                           :content welcome-finished}]})
    ;; Email to us
    (send-message conn {:from user-email
                        :to our-emails
                        :subject "Someone signed up!!"
                        :body (str firstname " registered for our service. Don't worry, we already emailed to tell them we love them. But maybe we should add their email to a list?")})))






(defn password-email
  "Sends a temporary password"
  [user-email password]

  (let [password-message (str "Here's your new temporary password. Remember to make yourself a new one after you log in! Your password is: " password)
        password-replacement-text [{:type :element, :attrs {:id "onTheClock", :style "margin: 1em 0;padding: 0;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;color: #606060;font-family: Helvetica;font-size: 15px;line-height: 150%;text-align: left;"}, :tag :p, :content [password-message]}]
        password-parsed (html-resource "templates/password-email.html")
        password-replaced (at password-parsed [:#whatWhat] (substitute password-replacement-text))
        password-finished (apply str (emit* password-replaced))]

    ;; Email to user
    (send-message conn
                  {:from "admin@lawsuitninja.com"
                   :to user-email
                   :subject "Shiny new password"
                   :body [:alternative
                          {:type "text/plain"
                           :content (str password-message " Your password is: " password)}
                          {:type "text/html"
                           :content password-finished}]})
    ;; Email to us
    (send-message conn {:from user-email
                        :to our-emails
                        :subject "Password replacement"
                        :body (str user-email " generated a random password. Kind of boring, but hey.")})))







(def password-changed-message "Sent if the user email can't support HTML."
  (str "If this wasn't you, reply to this email and let us know! We'll sort it out. Otherwise, just kick back and relax."))

(def password-changed-replacement-text "HTML with the right times and days of the week."
  [{:type :element, :attrs {:id "onTheClock", :style "margin: 1em 0;padding: 0;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;color: #606060;font-family: Helvetica;font-size: 15px;line-height: 150%;text-align: left;"}, :tag :p, :content [password-changed-message]}])
(def password-changed-parsed "Turns the HTML email template into Enlive code."
  (html-resource "templates/password-changed-email.html"))

(def password-changed-replaced "Replaces the default HTML with the better HTML."
  (at password-changed-parsed [:#whatWhat] (substitute password-changed-replacement-text)))
(def password-changed-finished "Turns the finished Enlive code back into real HTML."
  (apply str (emit* password-changed-replaced)))

(defn password-changed-email
  "Notifies the user that someone has changed their email."
  [user-email]
    ;; Email to user
    (send-message conn
                  {:from "admin@lawsuitninja.com"
                   :to user-email
                   :subject "Did you change your password?"
                   :body [:alternative
                          {:type "text/plain"
                           :content password-changed-message}
                          {:type "text/html"
                           :content password-changed-finished}]})
    ;; Email to us
    (send-message conn {:from user-email
                        :to our-emails
                        :subject "Password replacement"
                        :body (str user-email " replaced their password. Kind of boring, but hey.")}))
