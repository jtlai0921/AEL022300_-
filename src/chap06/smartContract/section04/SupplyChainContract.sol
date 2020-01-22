pragma solidity ^0.4.25;

contract SupplyChainContract {
    
 //合約主持人(銀行)
 address public bank;
 
 //製造商位址
 address private factory;
 
 //記錄一筆供應鏈交易
 struct SupplyTransaction {
  string transNo;   //交易憑單編號
  string transMemo; //交易說明
  address supplier; //供應商
  uint transTime;   //交易時間
  uint transValue;  //實體交易金額
  uint loanTime;    //放款時間
  uint loanValue;   //放款金額
  bool exist;
 }
    
 //儲存所有供應鏈交易
 mapping(uint => SupplyTransaction) public transData;
    
 //總供應鏈交易數
 uint public transCnt;
    
 //新增供應鏈交易事件
 event InsTransEvt(string indexed eventType, uint transCnt);
 
 //記錄合約主持人(銀行)
 constructor () public {
   bank = msg.sender;
 }
 	
 //只有銀行可執行 
 modifier onlyBank() {
   require(msg.sender == bank,
   "only bank can do this");
    _;
 }
 
 //只有製造商可執行 
 modifier onlyFactory() {
   require(msg.sender == factory,
   "only factory can do this");
    _;
 }
 
 //智能合約儲值
 function () public payable onlyBank{
 }
 
 //查詢智能合約餘額
 function queryBalance() public view onlyBank returns(uint){
  return address(this).balance;
 }
 
 //設定製造商位址  
 function setFactory(address _factory) public onlyBank {
  factory = _factory;
 }
	
 //查詢製造商位址  
 function queryFactory() public view returns(address) {
  return factory;
 }
 
 //新增一筆供應鏈交易
 function insSupplyTrans(string transNo,string transMemo, address supplier,uint transValue) public onlyFactory returns(uint){
   //供應鏈交易數量加1
   transCnt++;
         
   transData[transCnt].transNo = transNo; 
   transData[transCnt].transMemo = transMemo;   
   transData[transCnt].supplier = supplier;
   transData[transCnt].transValue = transValue;
   transData[transCnt].transTime = now;
   transData[transCnt].loanTime = 0;
   transData[transCnt].loanValue = 0;   
   transData[transCnt].exist = true;
  
   //觸發新增交易事件
   emit InsTransEvt("TransIns", transCnt);
   
   return transCnt;
 }
 
 //查詢交易是否存在
 function isTransExist(uint transKey) public view returns(bool) {
   return transData[transKey].exist;
 }
 
 //傳輸加密貨幣給供應商
 function loanEth(uint transKey, uint loanValue) public onlyBank{
   require(transData[transKey].exist, 
		 "transaction not exist");
   
   //設定放款金額
   transData[transCnt].loanValue = loanValue;
   
   //設定放款時間
   transData[transCnt].loanTime = now;
   
   //指定放款金額移轉供應商
   transData[transCnt].supplier.transfer(loanValue);
 }
}
