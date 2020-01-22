pragma solidity ^0.4.25;

contract OpenAuction {
    
    //version
    uint public ver=5;
    
	//拍賣開始時間
    uint public auctionStart;
	
	//拍賣期限
    uint public auctionLimit;
	
	//拍賣受益人
    address public beneficiary;

    //最高出價者
    address public highestBidder;

    //最高出價
    uint public highestBid;

	//拍賣開始旗標
    bool public startFlg;
	
    //拍賣結束旗標
    bool public endFlg;

    //更高出價事件
    event HighBidEvt(address bidder, uint amount);
	
	//拍賣開始事件
    event AuctionStartEvt(address starter);
	
    //拍賣結束事件
    event AuctionEndedEvt(address winner, uint amount);

	//出價事件
    event BidEvt(address bidder, uint amount);
	
    //進行出價
    function () public payable {
	    emit BidEvt(msg.sender, msg.value);
	
        //拍賣需已經開始
        require(startFlg, 
		  "auction not yet start");
		 
		//競標者需非最高出價者
		require(highestBidder != msg.sender,
            "You are highest bidder");
		
        //區塊時間需早於拍賣期限
        require(now <= auctionLimit,
            "auction ended");

        //出價金需高於最高金額
        require(msg.value > highestBid,
            "less than highest bid");

		//拍賣開始時的最高出價金為0
		//若不為0，代表已經有人出過價
		//應該將前一位出價者的價金退還
        if (highestBid != 0) {
          highestBidder.transfer(highestBid);
        }
		
		//記者新的最高出價者與金額
        highestBidder = msg.sender; 
        highestBid = msg.value;
         
        //發送更高出價事件
        emit HighBidEvt(msg.sender, msg.value);
    }

	//啟動拍賣活動
	function setAuctionStart(uint _timeLimit) public {
	   //拍賣需還沒設定已開始
       require(!startFlg, 
		 "auction already start");
		
	   beneficiary = msg.sender;
	   auctionStart = now;
		
	   //設定拍賣期限	
       auctionLimit = now + _timeLimit;
	   
	   //設定拍賣已開始
	   startFlg = true;
	   
	   //拍賣開始事件
	   emit AuctionStartEvt(msg.sender);
    }
	
    //結束拍賣
    function setAuctionEnd() public {
        //區塊時間需大於拍賣期限
        require(now >= auctionLimit, 
          "auction not yet ended");
        
        //拍賣需還沒設定已結束		
        require(!endFlg, 
		 "auction already ended");

        //設定拍賣結束
        endFlg = true;
        emit AuctionEndedEvt(highestBidder, highestBid);

        //將最高標金,移轉給主持人
        beneficiary.transfer(highestBid);
    }
}
