
# TutorHub

Tutorhub is a **fast, typing first address book for private tutors** to organise student details streamlining communication and tracking so tutors spend less time managing data and more time teaching.



* Table of Contents
{:toc}
--------------------------------------------------------------------------------------------------------------------

## Quick start


1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).


1. Download the latest `.jar` file from [here](https://github.com/AY2526S1-CS2103T-W10-1/tp/releases/tag/v1.3.0).


1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.


1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar tutorhub.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)


1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:


* `list` : Lists all contacts.


* `add type/s n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the address book.


* `delete 3` : Deletes the 3rd contact shown in the current list.


* `clear` : Deletes all contacts.


* `exit` : Exits the app.


1. Refer to the [Features](#features) below for details of each command.


--------------------------------------------------------------------------------------------------------------------


## Features


<div markdown="block" class="alert alert-info">


**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.


* Items in square brackets are optional.<br>
  e.g. `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.


* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.


* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.


* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.


* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>


### Viewing help : `help`


Shows a message explaining how to access the help page.


![help message](images/helpMessage.png)


Format: `help`




### Adding a person: `add`


Adds a person to the address book. There are 2 types of person to be added: Parent and Student. Simply indicate which one you wish to add with type/


Format: `add n/NAME type/TYPE p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​`


:bulb: **Tip:** A person can have any number of tags (including 0)


Examples:
* `add n/John Doe type/p p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy Crowe type/s e/betsycrowe@example.com a/Newgate Prison p/1234567 t/criminal`


### Listing all persons : `list`


Shows a list of all persons in the address book.


Format: `list`


### Editing a person : `edit`


Edits an existing person in the address book.


Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`


* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed e.g. adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
  specifying any tags after it.


Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.


### Locating persons by name: `find`


Finds persons whose names contain any of the given keywords.


Format: `find KEYWORD [MORE_KEYWORDS]`


* The search is case-insensitive. e.g. `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`


Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)


### Deleting a person : `delete`


Deletes the specified person from the address book.


Format: `delete INDEX`


* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​


Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.


### Clearing all entries : `clear`


Clears all entries from the address book.


Format: `clear`


### Linking Parents to Students : `link`


Tutors often need to know which parents belong to which students and vice versa. This feature allows maintaining a clear relationship map.


* Students can have multiple Parents (e.g., mother and father).
* Parents can be linked to multiple Students (e.g., siblings).
* The linkage is bidirectional: once linked, both profiles are updated

Format: `link student/INDEX parent/INDEX`

* Links the student to the parent at the specified `INDEX`s.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Example:
* `link student/1 parent/6`

All parents linked to a student will appear under the student's profile in the GUI as such:
```
   Parents: Alex Yeoh Mary Tan
```

### Unlinking Parents from Students: `unlink`


In the event of an accidental linkage, this unlinking feature will allow users to undo that.


Format: `unlink student/INDEX parent/INDEX`

* Unlinks the student from the parent at the specified `INDEX`es.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Example:
* `unlink student/2 parent/5`

### Adding a personal note: `note`

Adds a personal note under a student’s profile.

Format:  
`note INDEX note/NOTE`

* Adds a note to the student at the specified `INDEX`.
* The `INDEX` refers to the index number shown in the displayed person list.
* Duplicate notes are **allowed**.
* Notes longer than **50 characters** are not allowed.
* To delete the existing note(s) under a student, simply type `note INDEX` without adding a note value.


Examples:
* `note 1 note/Class president` — Adds a note under the first student.
* `note 2 note/Needs extra help in math` — Adds a note under the second student.
* `note 1` — Deletes the note(s) under the first student.

:bulb: **Tip:**
Use the `note` command to record useful or must-know information about a student that isn’t already captured by other fields.




### Assigning a lesson schedule: `schedule`


Assigns a fixed weekly day and time or a fixed date and time to an existing student.

* Assigns a weekly lesson schedule to the student at the specified `INDEX`.
* The `INDEX` refers to the index number shown in the displayed person list.
* The `DAY` must be one of `Monday`, `Tuesday`, `Wednesday`, `Thursday`, `Friday`, `Saturday`, or `Sunday` (case-insensitive).
* The `DATE` must be in MM-DD-YYYY format, e.g. `12-10-2025`, `04-29-2025`.
* The `TIME` must be in **24-hour format**, e.g. `08:00`, `16:45`, `10:30-12:00`.
* If a student already has a schedule, the old one will be **replaced** by the new schedule.
* To delete the existing schedule, simply type `schedule INDEX` without specifying any day or time.
                                                   

Format:
* `schedule INDEX schedule/DAY TIME`   
* `schedule INDEX schedule/DATE TIME`  
                           

Examples:
* `schedule 1 schedule/Monday 16:00-18:00` — Assigns a Monday schedule to the first student.
* `schedule 2 schedule/12-10-2025 09:00-10:30` — Assigns a fixed date schedule to the second student.
* `schedule 1` — Deletes the existing schedule under the first student.


The schedule appears in the GUI under the student’s profile as:
```
[Schedule] Monday 08:00-10:30
```

### Adding cost per lesson for each Student : `pay/`


Assigns a cost to a contact when adding them as contact

* Assigns a specific cost of lesson to the student
* COST must be a numeric value, e.g. 72.5
* To edit the COST of a specific student, simply type `edit INDEX ... pay/COST`

Format:                         
`Add type/s ... pay/COST`

Examples:
* `Add n/malcolm type/s ... pay/100` - Adds a student named malcolm whose cost per lesson is $100
* `Edit 1 pay/100` - edits contact at INDEX 1 to have cost be $100

The cost appears in the GUI under the student’s profile as:
```
[Cost] $100
```


### Checking who has paid and who hasn’t : `list paid` and `list unpaid`

Shows contacts filtered by payment status, or lists all contacts.

* list paid — shows only contacts whose payment status is Paid.
* list unpaid — shows only contacts whose payment status is Unpaid.
* Any other argument (e.g., list abc, list 3) is invalid and results in an error message.
* Arguments are case-insensitive (e.g., list Paid, list UNPAID work).

Format:          
* `list paid`    
* `list unpaid`  

### Tracking payment status of each Student : `paid`


Toggles payment status of specified contact between paid and unpaid.


Examples:
* `paid 1`-toggles payment status of contact at INDEX 1
* `paid n/Alex yeoh`- toggles payment status of contact named Alex Yeoh

:bulb: **Tip:**
Use the `paid` command to track whether a student has paid you for a lesson. The payment status will be displayed in the GUI using a checkbox in the top right corner. 


### Resetting payment status for every Student : `reset all`
Resets the payment status of all contacts (both Students and Parents) to unpaid.  
Useful for starting a new billing cycle (e.g., weekly or monthly) when all payments need to be cleared.

* Resets all contacts’ `PaymentStatus` to **unpaid (false)**.
* The command is **not case-sensitive** (e.g., `RESET ALL`, `Reset All` work).
* Only accepts the exact phrase `reset all`. Any extra words or tokens are rejected.
* Has no effect on contacts that are already unpaid.

Format:     
`reset all` 

Examples:
* `reset all` — Resets all contacts to unpaid.
* `RESET ALL` — Works the same (case-insensitive).
* `reset all now` — Invalid. Shows: *Error: invalid format. Use "reset all" only.*

:bulb: **Tip:**  
Use the `reset all` command at the start of each billing cycle to quickly clear all previous payment records.



### Exiting the program : `exit`


Exits the program.


Format: `exit`


### Saving the data


AddressBook data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.


### Editing the data file


AddressBook data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.


<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>



--------------------------------------------------------------------------------------------------------------------


## FAQ


**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous Tutorhub home folder.

**Q**: Why does parents not have a schedule field?  
**A**: Schedule field is meant to track classes for students and since parents could have multiple students linked to them, it would be better to only allow students to have a schedule.

**Q**: Can I import or export data in CSV format?  
**A**: Not in the current version. However, since data is stored as a JSON file, you can convert it manually to CSV using online tools if needed.

--------------------------------------------------------------------------------------------------------------------


## Known issues


1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.
3. You're unable to change the type of person from parent to student using the `edit` command, while you can change from student to parent. The remedy is to remove the ability to change a person's type altogether.


--------------------------------------------------------------------------------------------------------------------


## Command summary


Action | Format, Examples
--------|------------------
**Add** | `add type/s n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List** | `list`
**Help** | `help`
**Schedule** | `schedule INDEX schedule/DAY TIME-TIME`
**Note** | `note INDEX note/NOTE`
**Paid** | `paid INDEX` or `paid n/NAME`
**Reset all** | `reset all`
**Link** | `link student/INDEX parent/INDEX`
**Unlink** | `unlink student/INDEX parent/INDEX`


