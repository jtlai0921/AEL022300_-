pragma solidity ^0.4.25;

contract RegisterContract {
    
 //註冊合約主持人
 address public host;
 
 //總新聞合約數
 uint public contractCnt;
 
 //新聞合約ID與位址之對映
 mapping(uint => address) public idToAddrMapping;
    
 //新聞合約位址與暱稱之對映
 mapping(address => string) public addrToNameMapping;
     
 //註冊新聞合約事件
 event RegEvt(string indexed eventType, address contractAddr, string contractName);
 
 //記錄主持人
 constructor () public {
   host = msg.sender;
   contractCnt = 0;
 }
    
 //註冊新聞合約
 function regContract(address contractAddr, string contractName) public returns(uint){ 
   //合約暱稱不可空白
   require(bytes(contractName).length != 0,
     "contract name can not empty");
   
   //合約位址不可重覆
   require(bytes(addrToNameMapping[contractAddr]).length == 0,
 	"contract address alredy registry");
	
	//合約總量加1
   contractCnt++;
         
   //記錄合約ID與位址之對映   
   idToAddrMapping[contractCnt] = contractAddr;

   //記錄合約位址與暱稱之對映		 
   addrToNameMapping[contractAddr] = contractName;
 
   //觸發新增新聞合約事件
   emit RegEvt("RegEvn", contractAddr, contractName);
   
   //回傳註冊後的主鍵
   return contractCnt;
 }
 
 //查詢合約位址是否已註冊
 function isContractExist(address contractAddr) public view returns(bool) {
   if (bytes(addrToNameMapping[contractAddr]).length == 0)
     return false; //未註冊
   else
     return true; //已註冊
 }
}
