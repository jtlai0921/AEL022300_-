pragma solidity ^0.4.25;

contract EthPump {
 
 //合約主持人EOA
 address private host;
 
 //物品資訊
 struct Goods {
   address owner;	//出借人EOA
   address borrower;//借用人EOA
   uint ethPledge;	//押金
   bool available;  //是否已上架
   bool isBorrow;   //是否已借出
   bool exist;
 }
 
 //儲存所有貼紙(分類)資訊
 mapping(string => mapping(uint => Goods)) private goodsData;
 
 //所有貼紙(分類)物品筆數
 mapping(string => uint) private goodsInx;
 
 //貼紙(分類)是否存在之記錄
 mapping(string => bool) private goodsChk;
 
 //記錄合約主持人
 constructor () public {
   host = msg.sender;
 }
 	
 //只有主持人可執行 
 modifier onlyHost() {
   require(msg.sender == host,
   "only host can do this");
    _;
 }

 //查詢貼紙(分類)是否已經存在
 function isStickExist(string stickName) public view returns(bool) {
   return goodsChk[stickName];
 }
 
 //新增一種貼紙(分類)
 function addSticker(string stickName) public onlyHost {
  //貼紙(分類)不存在，才可以新增
  require(!isStickExist(stickName), 
		 "stick already exist");
		 
   //設定可以使用此類貼紙
  goodsChk[stickName] = true; 
  
  //觸發新增貼紙的事件
  emit addStickerEvnt("addSticker", stickName);
 }

 //新增貼紙(分類)事件
 event addStickerEvnt(string indexed eventType, string stickName);
 
 //新增物品
 function addGoods(string stickName, uint ethPledge, bool available) public returns(uint){
  //貼紙(分類)必須存在 
  require(isStickExist(stickName), 
		 "stick not exist");
		 
   //物品序號加1   
   goodsInx[stickName] +=1;
   uint inx = goodsInx[stickName];
   
   //新的物品資訊
   Goods memory goods = Goods({
	 owner: msg.sender,		//出借人EOA
	 borrower: 0,			//借用人EOA
	 ethPledge: ethPledge,	//押金
	 available: available,	//是否已上架
	 isBorrow: false,		//是否已借出 
	 exist: true			//確認資訊存在
   });
   
   //資料儲存至映射結構
   goodsData[stickName][inx] = goods;
   
   //觸發新增物品事件
   emit addGoodsEvnt("addGoods", stickName, inx);
   
   //回傳資料索引
   return inx;
 }
 
 //新增物品事件
 event addGoodsEvnt(string indexed eventType, string stickName, uint inx);
 
 //判斷物品是否存在
 function isGoodExist(string stickName, uint inx) public view returns(bool){
   //貼紙(分類)必須存在 
   require(isStickExist(stickName), 
		 "stick not exist");
   
   return goodsData[stickName][inx].exist;   
 }
 
 //設定物品上下架
 function setGoodsStatus(string stickName, uint inx, bool available) public { 
   //物品必須存在 
   require(isGoodExist(stickName, inx), 
		 "goods not exist");
	
   //必須是出借人,才可以改變狀況	
   require(goodsData[stickName][inx].owner == msg.sender,
           "not goods owner");
		   
   //物品必須沒被借出	
   require(!goodsData[stickName][inx].isBorrow,
           "goods already lend");
	
   //改變上下架狀態	
   goodsData[stickName][inx].available = available;
 }
 
 //查詢物品是否上下架
 function isGoodsAvailable(string stickName, uint inx) public view returns(bool) { 
   //物品必須存在 
   require(isGoodExist(stickName, inx), 
		 "goods not exist");
	
   //回傳上下架狀態	
   return goodsData[stickName][inx].available;
 }
 
 //查詢物品借出狀態
 function isGoodsLend(string stickName, uint inx) public view returns(bool) { 
   //物品必須存在 
   require(isGoodExist(stickName, inx), 
		 "goods not exist");
	
   //回傳借出狀態	
   return goodsData[stickName][inx].isBorrow;
 }
 
 //借出物品
 function borrowGoods(string stickName, uint inx) public payable { 
   //物品必須存在 
   require(isGoodExist(stickName, inx), 
		 "goods not exist");
   
   //物品必須是可用狀態
   require(goodsData[stickName][inx].available,
           "goods not available");
		   
   //物品必須沒被借出	
   require(!goodsData[stickName][inx].isBorrow,
           "goods already lend");
	
   //押金必要符合設定
   require(goodsData[stickName][inx].ethPledge == msg.value,
           "eth pledge not match");
		   
   //設定借用人EOA
   goodsData[stickName][inx].borrower = msg.sender;
   
   //設定為已借出
   goodsData[stickName][inx].isBorrow = true;
   
   //觸發借出事件
   emit borrowGoodsEvnt("borrowEvn", stickName, inx, msg.sender);
 }
 
 //物品借出事件
 event borrowGoodsEvnt(string indexed eventType, string stickName, uint inx, address borrower);
 
 //查詢物品借出人
 function queryBorrower(string stickName, uint inx) public view returns(address) { 
   //物品必須存在 
   require(isGoodExist(stickName, inx), 
		 "goods not exist");
	
   //物品必須已被借出	
   require(goodsData[stickName][inx].isBorrow,
           "goods not lend");
		   
   //回傳借出人
   return goodsData[stickName][inx].borrower;
 }
 
 //設定物品已歸還
 function doGoodsReturn(string stickName, uint inx) public { 
   //物品必須存在 
   require(isGoodExist(stickName, inx), 
		 "goods not exist");
	
   //必須是出借人,才可以改變狀況	
   require(goodsData[stickName][inx].owner == msg.sender,
           "not goods owner");
		   
   //物品必須已被借出	
   require(goodsData[stickName][inx].isBorrow,
           "goods not lend");
	
   //將押金返還借用人
   uint pledge = goodsData[stickName][inx].ethPledge;
   goodsData[stickName][inx].borrower.transfer(pledge);
   
   //觸發歸還事件
   emit returnGoodsEvnt("returnEvn", stickName, inx, goodsData[stickName][inx].borrower);
   
   //設定借用人EOA
   goodsData[stickName][inx].borrower = 0;
   
   //設定為未借出
   goodsData[stickName][inx].isBorrow = false;
 }
  
 //物品歸還事件
 event returnGoodsEvnt(string indexed eventType, string stickName, uint inx, address borrower);
 
  //查詢合約餘額
  function queryBalance() public view returns (uint) {
	return address(this).balance;
  }	
}