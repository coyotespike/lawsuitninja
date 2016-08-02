(ns lawsuitninja.expunction
  (:require  [clojure.string :refer [upper-case blank? join split]])
  (:use clj-pdf.core
        ring.util.io))


(def gov-agencies
  [
   [["TEXAS DEPARTMENT OF PUBLIC SAFETY"]
    ["DPS CRIMINAL RECORDS DIVISION"]
    ["Individually and for the Federal Bureau of Investigation"]
    ["P.O. Box 4143"]
    ["Austin, Texas 78765-4143"]]

   [["AUSTIN POLICE DEPARTMENT"]
    ["P.O. Box 689001 "]
    ["Austin, Texas 78768-9001"]]

   [["SHERIFF OF TRAVIS COUNTY, TEXAS"]
    ["P.O. Box 1748"]
    ["Austin, Texas 78767"]]

   [["COUNTY ATTORNEY’S OFFICE OF TRAVIS COUNTY, TEXAS"]
    ["P.O. Box 1748"]
    ["Austin, Texas 78767"]]

   [["COUNTY CLERK OFFICE OF TRAVIS COUNTY, TEXAS"]
    ["P.O. Box 1748"]
    ["Austin, Texas 78767"]]

   [["PRETRIAL SERVICES—PERSONAL BOND"]
    ["TRAVIS COUNTY, TEXAS"]
    [" P.O. Box 1748"]
    ["Austin, Texas 78767"]]

   [["AUSTIN MUNICIPAL COURT"]
    ["CITY OF AUSTIN"]
    ["700 East 7th"]
    ["Austin, Texas 78701"]]

   [["TRAVIS COUNTY COUNSELING AND EDUCATION SERVICES"]
    ["P.O. Box 1748"]
    ["Austin, Texas 78767"]]

   [["TRAVIS COUNTY INFORMATION TECHNOLOGY SERVICES"]
    ["Attn:  Janice Brown"]
    ["Application Support"]
    ["700 Lavaca"]
    ["Austin, Texas 78701"]]

   [["TEXAS DEPARTMENT OF PUBLIC SAFETY HEADQUARTERS"]
    ["GOVERNOR’S DIVISION OF EMERGENCY MANAGEMENT"]
    ["Attn:  Johanna Cantrel"]
    ["P. O. Box 4087"]
    ["Austin, Texas 78733-0220"]]
   
   [["DIRECTOR OF HOMELAND SECURITY"]
    ["OFFICE OF HOMELAND SECURITY"]
    ["GOVERNOR’S OFFICE"]
    ["P. O. Box 12428"]
    ["Austin, Texas  78714"]]])

(defn agency-pdf-cell
  [thing]
  [[:pdf-cell {:valign :middle} (first thing)]])

(defn many-agency-cells
  [myvec]
  (let [firstguy [[:pdf-cell {:valign :middle} (first myvec)]]
        others (map agency-pdf-cell (rest myvec))]
  (apply vector firstguy others)))

(defn many-agency-cells
  [myvec]
  (apply vector (map agency-pdf-cell myvec)))

(defn bold-first
  [anothervec]
  (let [celled (many-agency-cells anothervec)]
    (assoc-in celled [0 0 1] {:valign :middle :styles [:bold]})))

(defn make-agency-table
  [myvec]
  [(apply vector
         :pdf-table {:cell-border false :horizontal-align :justified :width-percent 100}
   [100]
   (bold-first myvec))])

(defn make-big-table
  [myvec]
  (apply vector
         :pdf-table {:cell-border false :horizontal-align :center :width-percent 80}
   [100]
   (map make-agency-table myvec)))
  
(defn make-pdf-cell 
  "Accepts a vector of two items, returns the correct pdf-cell vector for pdf-table"
  [pair]
  [[:pdf-cell {:valign :middle} (first pair)]
   [:pdf-cell {:valign :middle} (second pair)]])

(defn make-many-cells
  [myvec]
  (map make-pdf-cell myvec))

(defn make-pdf-table
  "Accepts a vector containing arbitrary number of vectors with two items each.
  Returns a pdf-table with an appropriate number of cells"
  [myvec]
  (apply vector
  :pdf-table {:cell-border false :horizontal-align :justified :width-percent 100}
   [50 50]
   (make-many-cells myvec)))


(defn return-pdf 
  "I was unable to figure out how to dynamically redefine the vars.
  Therefore, I have wrapped them all in this function.
  The function evaluates the request, and takes out user data.
  It then plugs the user data into var vectors/maps, and then applies
  the templates to those vars.
  The function returns the completed pdf."
  []
  (let [data {:yourname "Timothy Roy"
              :empty-precinct "Precinct"
              :empty-place "Empty place?"
              :landlordname "Bad guy"
              :your-county "Travis"
              :landlord-address "123 Here"
              :the-facts "He did bad stuff I guess"
              :your-address "I live her"
              :city-state "Austin, Texas"
              :race "White"
              :sex "Male"
              :birthdate "12/13/1984"
              :driverlicense "123456789"
              :ssn "0987654321"
              :arrest-address "1234 Bummer Lane, Austin TX 78753"
              :charge "Theft Class B Misdemeanor"
              :offensedate "January 1, 2016"
              :arrestcounty "Travis County, Texas"
              :arrestmunicipality "Austin, Texas"
              :arrestingagency "Austin Police Department"
              :offensereportno "11-091257"
              :offensecourt "Austin Municipal Court"
              :offensecausenumber "C1-CR-01-20548"
              :informationcourt "County Court at Law Number 6, Travis County"
              :informationcourtnumber "C1-CR-01-20548"
              :trn-no "123409874123"
              }

 plaintiff  [{:plaintiff-name (:yourname data)
              :precinct (:empty-precinct data)
              :place (:empty-place data)
              :defendant-name (:landlordname data)
              :county (:your-county data)
              :initials "FML"}]

;chapter1 [{:section-heading "DISCOVERY" :number "I. "}]
chapter1 [{:number "I. "}]
chapter2 [{:number "II. "}]
chapter3 [{:number "III. "}]
chapter4 [{:number "IV. "}]
chapter5 [{:number "V. "}]
chapter6 [{:number "VI. "}]
verheading [{:number "Verification "}]

content1 [{:content 
           [:paragraph
            [:chunk {:styles [:bold]} "Personal Information:"]]}]

personal-info [["Name:" (:yourname data)]
               ["Race:" (:race data)]
               ["Sex:" (:sex data)]
               ["Date of birth:" (:birthdate data)]
               ["Driver's License Number:" (:driverlicense data)]
               ["Social Security Number:" (:ssn data)]
               ["Address at Time of Arrest:" (:arrest-address data)]]

content1 
        [:paragraph
         [:chunk {:styles [:bold]} "Personal Information:"]
         (make-pdf-table personal-info)
         [:spacer 1]]


offense-info [["Offense Charged:" (:charge data)]
               ["Date of Offense:" (:offensedate data)]
               ["County of Arrest:" (:arrestcounty data)]
               ["Municipality of Arrest:" (:arrestmunicipality data)]
               ["Arresting Agency:" (:arrestingagency data)]
               ["Offense Report Number:" (:offensereportno data)]
               ["Complaint filed in:" (:offensecourt data)]
               ["Cause Number: " (:offensecausenumber data)]
               ["Information filed in: " (:informationcourt data)]
               ["Cause Number:" (:informationcourtnumber data)]
               ["Texas DPS TRN:" (:trn-no data)]]
content2
        [:paragraph
         [:chunk {:styles [:bold]} "Offense Information:"]
         (make-pdf-table offense-info)
         [:spacer 1]]

content3
        [:paragraph {:leading 25}
         [:chunk {:styles [:bold]} "Petitioner is entitled to an expunction of the above arrest, because:"]

         [:list {:numbered true}
         [:paragraph (str
"An information charging the Petitioner with the commission of a " (:charge data) " arising out of the arrest was filed in " (:informationcourt data) ". The case was dismissed.  (Exhibit A attached hereto)  The statute of limitations has expired.")
          [:spacer 1]]
         [:paragraph
"Petitioner was released and the charge, if any, has not resulted in a final conviction and is no longer pending, and there was no court-ordered supervision under Article 42.12 of the Texas Code of Criminal Procedure, and there was no indictment nor felony offense filed arising out of this arrest."]]]


content4 [{:content "The amount in controversy is within the jurisdictional limits of of this Court, and venue is proper as the cause of action arose in the county of this Court within Texas."
                :number "4. "}]

content5 [{:content "Petitioner has reason to believe the following governmental agencies may have records or files pertaining to Petitioner in connection with the above arrest: "
           }]

content6 [{:content 

"Pursuant to Article 55002(3)(c-2), Texas Code of Criminal Procedure, Petitioner moves this Court to order the Department of Public Safety to give notice of its Order to all individuals and entities named in the Order and to those which purchase information from the Department. 

WHEREFORE, PREMISES CONSIDERED, Petitioner prays the Court set this matter for hearing, and after reasonable notice to the parties, order records of the above arrest expunged pursuant to Chapter 55 of the Code of Criminal Procedure. "
}]

sig-content [{:name (:yourname data)
                   :address (:your-address data)
                   :city-state-zip (:city-state data)
                   :phone "123-456-7890?"}]

heading-template

  (template
   [:paragraph {:align :center}
    [:chunk {:styles [:bold] :size 10} "Cause No. _________________"]  
    [:pdf-table {:cell-border false :horizontal-align :justified :width-percent 100}
     [30 25 35]

     [[:pdf-cell {:valign :middle} "EX PARTE," ]
      [:pdf-cell {:align :center :valign :middle} "§" ]
      "IN THE DISTRICT COURT"]

     [[:pdf-cell {:valign :middle} "" ]
      [:pdf-cell {:align :center :valign :middle} "" ]
      ""]

     [[:pdf-cell {:valign :middle} ""]
      [:pdf-cell {:align :center :valign :middle} "§" ]
      [:pdf-cell {:align :center :valign :middle} (str "OF " (upper-case $county) " COUNTY, TEXAS")]]
;      [:pdf-cell (str "PRECINCT " $precinct ",")]]

     [[:pdf-cell {:valign :middle} "" ]
      [:pdf-cell {:align :center :valign :middle} "" ]
      [:pdf-cell ""]]

     [[:pdf-cell {:valign :middle} "" ]
      [:pdf-cell {:align :center :valign :middle} "" ]
      [:pdf-cell ""]]

     [[:pdf-cell {:valign :middle} $initials]
      [:pdf-cell {:align :center :valign :middle} "§" ]
      [:pdf-table 
       {:cell-border false :horizontal-align :center :width-percent 100}
       [10 25]
       [[:pdf-cell {:styles [:underline]} "15th"]
        [:pdf-cell {:align :center :valign :middle} " JUDICIAL DISTRICT"]]]]

     [[:pdf-cell {:valign :middle} "" ]
      [:pdf-cell {:align :center :valign :middle} "" ]
      ""]

     [[:pdf-cell ""]
      [:pdf-cell {:colspan 3 :align :justified :styles [:bold :underline] :padding-top 15} "PETITION FOR EXPUNCTION" ]
      ""]

     ["" "" ""]
     ["" "" ""]
     ]])

 salutation
        (template
  [:paragraph    
   {:indent 20}


   [:chunk "TO THE HONORABLE JUDGE OF THE COURT:"] "\n" "\n"
   [:paragraph {:spacing 2}
    [:phrase {:style :bold} "COMES NOW "] 
    $plaintiff-name ", Petitioner, and moves the Court to order to expunction of 

all criminal records and files pertaining to the arrest of Petitioner described below."
    "\n""\n"
    "In support whereof, Petitioner would show the following:"
]
   [:spacer 1]
])

 section-heading 
  (template
   [:paragraph {:align :center :style :bold} $number $section-heading
   [:spacer]])


 section-content
  (template
   [:paragraph {:first-line-indent-number 50 :leading 25 :align :left} $number $name $content $address $lister
    [:spacer]])

verification
[:paragraph
 [:paragraph {:align :center :style :bold} "Verification"]
 [:spacer 2]
 [:paragraph {:first-line-indent-number 50 :leading 25 :align :left}
  (str "BEFORE ME, the undersigned authority, personally appeared " (upper-case (:yourname data)) ", who, having been duly sworn, stated:")]
 [:paragraph {:first-line-indent-number 50 :leading 25 :align :left}
  (str "\"My name is " (:yourname data) ". I am the Petitioner in the above Petition for Expunction. I have read said Petition, and the facts stated therein are true and correct.\"")
  ]]

 signature-block
  (template
   [:paragraph {:align :right}
    [:list {:symbol ""}
     [:chunk "Respectfully submitted, "]
     [:chunk "____________________________"]
     [:chunk $name]
     [:chunk $address]
     [:chunk $city-state-zip]
     [:chunk $phone]]])]


    (piped-input-stream
     (fn [output-stream]

  (pdf
   [{:font {:size 12
            :family :times-roman
            :spacing 2}
     :size :letter
     :right-margin 80
     :left-margin 80
     :top-margin 20
     :bottom-margin 10}

    (heading-template plaintiff)
    (salutation plaintiff)
    (section-heading chapter1)
;    (make-pdf-table personal-info)
    content1
    (section-heading chapter2)
    content2
    (section-heading chapter3)
    content3
    (section-heading chapter4)
    (section-content content5)
    (make-big-table gov-agencies)


 ;   (section-heading chapter5)
    (section-content content6)
;    (section-content content6)
    (signature-block sig-content)
    [:pagebreak]
    (section-heading verheading)
;    verification
    ]
   output-stream)))))

(defn make-pdf2
  "Returns the completed template, with appropriate content headers."
  [req]
    {:headers {"Content-Type" "application/pdf"}
     :body (return-pdf)})
