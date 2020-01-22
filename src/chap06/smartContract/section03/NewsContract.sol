pragma solidity ^0.4.25;

contract NewsContract {
    
 //新聞主持人
 address public host;
 
 //記錄一筆新聞
 struct News {
  string newsCxt;
  uint accumulate;
  bool exist;
 }
    
 //儲存所有新聞
 mapping(uint => News) public newsData;
    
 //總新聞數
 uint public newsCnt;
 
 //得到最多獎勵的新聞主鍵
 uint public maxRewardNews;
    
 //新增新聞事件
 event AddNewsEvt(string indexed eventType, uint newsKey);
 
 //獎勵新聞事件
 event RewardEvt(string indexed eventType, address sender, uint value);
 
 //記錄受益人
 constructor () public {
   host = msg.sender;
 }
 
 //只有主持人可執行 
 modifier onlyHost() {
   require(msg.sender == host,
   "only host can do this");
    _;
 }
    
 //新增一則新聞
 function addNews(string newsCxt) public onlyHost returns(uint) {
   //發佈總量加1
   newsCnt++;
         
   newsData[newsCnt].newsCxt = newsCxt;
   newsData[newsCnt].accumulate = 0;
   newsData[newsCnt].exist = true;
 
   //觸發新增新聞事件
   emit AddNewsEvt("NewsAdd", newsCnt);
      
   return newsCnt;
 }
 
 //獎勵一則新聞
 function rewardNews(uint newsKey) public payable {
   //主持人不可以獎勵自己
   require(msg.sender != host,
 	"host can not reward himself");
 
   //獎勵金需大於0
   require(msg.value > 0,
     "reward value need grater than 0");
 	
   //新聞必須存在  
   require(newsData[newsKey].exist,
     "news not exist");
 	  
   //累加獎勵  
   newsData[newsKey].accumulate += msg.value;
     
   //判斷是否置換最高獎勵的新聞
   if (newsData[newsKey].accumulate > newsData[maxRewardNews].accumulate)
    maxRewardNews = newsKey;
 
   //觸發獎勵事件
   emit RewardEvt("Reward", msg.sender, msg.value);
 }
 
 //查詢新聞是否存在
 function isNewsExist(uint newsKey) public view returns(bool) {
   return newsData[newsKey].exist;
 }
    
 //閱讀新聞內容
 function queryCtx(uint newsKey) public view returns(string) {
   //新聞必須存在  
   require(newsData[newsKey].exist,
 	  "news not exist");
 	  
   return newsData[newsKey].newsCxt;
 }
    
 //查詢新聞累積獎勵
 function queryReward(uint newsKey) public view returns(uint) {
  //新聞必須存在  
  require(newsData[newsKey].exist,
 	  "news not exist");
 	  
  return newsData[newsKey].accumulate;
 }

 //接收獎勵
 function getReward() public onlyHost{
  host.transfer(address(this).balance);
 }
}
