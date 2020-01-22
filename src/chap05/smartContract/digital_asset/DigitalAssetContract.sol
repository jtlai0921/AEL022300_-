pragma solidity ^0.4.25;

contract DigitalAssetContract {

	//購買事件	 
	event BuyEvent(string indexed eventType, address buyer, uint256 money);
	
	//錯誤事件	 
	event ErrEvent(string indexed eventType, address buyer, uint256 money);
	
	//設定事件	 
	event SetEvent(string indexed eventType, address buyer, string license);
	
    //記錄數位資產
    mapping(address => string) assetDatas;
    			
	//購買數位資產
    function () public payable {
	  //以1ETH購買
	  if (1000000000000000000 == msg.value) {
	     //引發購買事件
	    emit BuyEvent("buy", msg.sender, msg.value);
	  } else {
	    emit ErrEvent("err", msg.sender, msg.value);
	  }
    }
    
	//設定持有人的數位憑證
    function setOwner(address owner, string license) public {
      assetDatas[owner] = license;
	  
	  emit SetEvent("set", owner, license);
    }
	
    //查詢持有人的數位憑證
    function queryByOwner(address owner) public view returns (string license) {
        return assetDatas[owner];
    }
}