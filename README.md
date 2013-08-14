Siemens-Project-LLG
===================

Siemens Project for Computer Science

If this is the first time you run the code, you should click on "Generate Key Pairs" button first, so the public and private key are generated and can be used to encrypt a secret master key called AES key which is then used to encrypt and decrypt the big files for fast performance. 

To run this file, you need download Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files 7 from

http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html

and extract and save two policy jar files 
To your local PC at

C:\Program Files\Java\jre7\lib\security

 You need to copy and replace what you have in this folder. Your folder may be different, depends on your%JRE_HOME%

The reason is that we are using strong encryption and it is restricted by export policy. So, please don't use this code outside of USA.
