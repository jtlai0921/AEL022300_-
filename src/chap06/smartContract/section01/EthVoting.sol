pragma solidity ^0.4.25;

contract EthVoting {

    //附議人資訊
    struct Voter {
        uint voteTimeStamp; //投票時的區塊時間
        bool initialized;   //判斷是否投過票的旗標
    }

    //提案內容
    struct Proposal {
        string pName;        //提案標題
		string pCtx;         //提案內容
		address chairperson; //提案主持人
        uint voteCount;      //附議人數
        bool initialized;    //判斷提案是否存在的旗標
        uint limitTime;      //附議限制時間
		mapping(address => Voter) voters; //附議列表
    }

    //所有提案列表
    mapping(uint => Proposal) public proposals;
	
	//附議事件
	event VoteEvt(string indexed eventType, address _voter, uint timestamp);
	
	//提案事件
	event ProposeEvt(string indexed eventType, uint _proposalId, uint _limitTime);
	
	//建立新提案
	function createProposal(string _pName, string _pCtx, uint _limitTime) public returns (uint){
	  //新提案
	  Proposal memory proposal = Proposal({
                pName: _pName,
				pCtx: _pCtx,
				chairperson: msg.sender,
				initialized: true,
				limitTime: _limitTime,
                voteCount: 0 });
      
      //以區塊時間，做為ID	  
      uint pId = block.timestamp; 
      proposals[pId] = proposal;
      emit ProposeEvt("propose", pId, _limitTime);
      
      return pId;
	}
	
	//進行附議
	function doVoting(uint pId) public {
	  //提案是否存在
	  if (proposals[pId].initialized == false)
	    revert("proposal not exist");
	  
	  uint currentTime = block.timestamp;
	  
	  //是否已超過提案時限
	  if (proposals[pId].limitTime < currentTime)
	    revert("exceed voting time");
	  
	  //是否已經投過票
	  if (proposals[pId].voters[msg.sender].initialized == true)
	   revert("already vote");
	  
	  //新投票資訊
	  Voter memory voter = Voter({
	     voteTimeStamp: block.timestamp,
	     initialized: true
	  });
    
	  //記錄投票資訊
	  proposals[pId].voters[msg.sender] = voter;
	  proposals[pId].voteCount+=1;
	  
	  emit VoteEvt("vote", msg.sender, block.timestamp);
	}	
	
	//查詢是否附議
	function queryVoting(uint pId, address voterAddr) public view returns (uint){
	  //提案是否存在
	  if (proposals[pId].initialized == false)
	    revert("proposal not exist");
	  
	  //回傳投票時間
	  return proposals[pId].voters[voterAddr].voteTimeStamp;
	}	
	
	//取得區塊鏈時間
    function getBlockTime() public view returns (uint t) { 
     t = block.timestamp;
    }

    //查詢提案標題
    function getProposalName(uint pId) public view returns (string s) { 
     s = proposals[pId].pName;
    } 	
	
	//查詢提案內容
    function getProposalCtx(uint pId) public view returns (string s) { 
     s = proposals[pId].pCtx;
    }
	
	//查詢提案內容
    function getProposalVCnt(uint pId) public view returns (uint v) { 
     v = proposals[pId].voteCount;
    }
	
	//查詢提案期限
    function getProposalLimit(uint pId) public view returns (uint t) { 
     t = proposals[pId].limitTime;
    }
}

