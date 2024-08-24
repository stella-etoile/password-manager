## Password Manager (ver. 2.3.0)
### Current functionalities
1) Recovering passwords
2) Adding new passwords
3) Changing previous passwords
4) Sorting passwords by name
    - i.e. Given a set of passwords
        - {'google-stellarium01','google-stellarium02', 'github-stellarium01'}
    - You can sort by 'google' and retrieve
        - {'stellarium01', 'stellarium02'}
5) Multiple files access
    1) Set a default target file that your program launches into.
    2) Change the target file temporarily.
    3) Change your default target file. 
        - i.e. 'list.txt' for passwords, 'encoded-backupcodes.txt' for encoded backup codes for 2FA logins.
    4) Reset the list of possible target files
6) Automatically check Github repo for updates.

---

### Functionalities to be added
1) Changing name of websites
    - i.e. Change from 'testwebsite1' to 'testwebsite2' after a typo
2) Allow for different file extensions besides '.txt'
3) Deleting password files if wanted

---
### Changelogs:
#### 1.0.0 (Oct 18, 2021): initial version of the program that has the functionality to
    1) recover passwords
    2) add passwords
    3) change passwords (doesn't work but can be circumvented by adding a password with the same name again replacing the old password)
#### 2.0.0 (Apr 13, 2024): recoded version of version 1.0.0 with the bugfix of the inability to change passwords
    3) change passwords
#### 2.0.1 (Apr 27, 2024): fixed the batch file used to run 'manager.jar' to run in the current directory without having to manually set it
#### 2.1.0 (Aug 5, 2024): added sorting functionality to the usernames functionalities include 
    4) sorting passwords
        - i.e. Given a set of passwords
            - {'google-stellarium01','google-stellarium02', 'github-stellarium01'}
        - You can sort by 'google' and retrieve
            - {'stellarium01', 'stellarium02'}
#### 2.1.1 (Aug 5, 2024): bugfix
#### 2.2.0 (Aug 23, 2024): added the functionality to be able to create multiple files, set a default target file, change the target file temporarily, change the default target file
    5) Multiple files access
        1) Set a default target file that your program launches into.
        2) Change the target file temporarily.
        3) Change your default target file. 
            - i.e. 'list.txt' for passwords
            - 'encoded-backupcodes.txt' for encoded backup codes for 2FA logins.
#### 2.2.1 (Aug 23, 2024): fixed a bug that created multiple instances of the same program looping in main and a bug that didn't properly delete the buffer strings when using 'java.util.Scanner'
    1) Bug where it created multiple instances of the same program looping in main
        - this was caused because all the instances of main were not being closed together when exitting
        - in order to fix this issue, a global variable 'exit_all' was created to synchronize the exitting of the programs in every instances
            - inspired by the movie Inception with how the waking of the dream is synchronized by a certain key
    2) Bug where the buffers were not deleted correctly when using 'java.util.Scanner'
        - the problem arose when in a layer of the program past its original layer
        - i.e. an instance within an instance
        - in such occasion, the program ran and looked for an input even though the input didn't exist
        - this was fixed by not closing the scanners until the program is finished executing (this might cause memory leaks? might need to look into using a global variable for scanner)
    

---

### Known bugs:
1) Possible memory leaks if the program has too many iterations of itself (might need to look into using a global scanner for each iterations)
2) There are sometimes when a .txt file created and deleted instantly so that the user can copy-paste the decrypted password doesn't actually get deleted.