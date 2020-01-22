c:\jdk1.8\bin\java -cp .;.\lib\* org.web3j.codegen.SolidityFunctionWrapperGenerator ./RegisterContract.bin ./RegisterContract.abi -o ./java -p chap06.com.alc

c:\jdk1.8\bin\java -cp .;.\lib\* org.web3j.codegen.SolidityFunctionWrapperGenerator ./NewsContract.bin ./NewsContract.abi -o ./java -p chap06.com.alc

rem geth attach ipc:\\.\pipe\geth.ipc

rem loadScript("RegisterContract.js");

rem loadScript("NewsContract.js");