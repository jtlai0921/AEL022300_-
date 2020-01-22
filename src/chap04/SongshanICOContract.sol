pragma solidity ^0.4.24;

contract SongshanICOContract {

    string public constant name = "Songshan Token";	
	string public constant symbol = "STC";
    uint8 public totalSupply = 5; //Token總量
    
	event Transfer(address indexed _from, address indexed _to, uint _value);
    event Approval(address indexed _owner, address indexed _spender, uint _value);
	 
	event DoICO(address buyer, uint256 ethCoin, uint8 totalSupply);
	 
    //合約建立者addr
    address public owner;
 
    //儲存Token餘額
    mapping(address => uint8) balances;
    
    //授權表
    mapping(address => mapping (address => uint256)) allowed;
 
    //owner可執行的標註
    modifier onlyOwner() {
      if (msg.sender != owner) {
        revert();
      }
      _;
    }
 
    //建構者函數
    constructor() public{
     owner = msg.sender;	 
    }
				
	//購買ICO幣
    function () public payable {
	
	emit DoICO(msg.sender, msg.value, totalSupply);
	
	  //條件1. 是否還有可以交易的代幣
	  //條件2. 購買金額是否 1 ETH
	  //條件3. 是否未曾購買
	  if (totalSupply > 0 &&
	      1000000000000000000 == msg.value &&
	        
		  balances[msg.sender] == 0) {
	   
		  //總量減1
		  totalSupply -= 1;
		  
		  //記錄買一個代幣
		  balances[msg.sender] = 1;	       		
	   } else {
	     //不符合任一條件
	     revert();
       }
    }
    
 
    //指定帳號的代幣餘額
    function balanceOf(address _owner) public view returns (uint256 balance) {
        return balances[_owner];
    }
 
    //指定轉入帳號之代幣移轉
    function transfer(address _to, uint8 _amount) public returns (bool success) {
        if (balances[msg.sender] >= _amount 
            && _amount > 0
            && balances[_to] + _amount > balances[_to]) {
            balances[msg.sender] -= _amount;
            balances[_to] += _amount;
            emit Transfer(msg.sender, _to, _amount);
            return true;
        } else {
            return false;
        }
    }
 
    //指定轉出與轉入帳號之代幣移轉
    function transferFrom(
        address _from,
        address _to,
        uint8 _amount
    ) public returns (bool success) {
        if (balances[_from] >= _amount
            && allowed[_from][msg.sender] >= _amount
            && _amount > 0
            && balances[_to] + _amount > balances[_to]) {
            balances[_from] -= _amount;
            allowed[_from][msg.sender] -= _amount;
            balances[_to] += _amount;
            emit Transfer(_from, _to, _amount);
            return true;
        } else {
            return false;
        }
    }
 
    //指定數量之轉出帳號授權
    function approve(address _spender, uint8 _amount) public returns (bool success) {
        allowed[msg.sender][_spender] = _amount;
        emit Approval(msg.sender, _spender, _amount);
        return true;
    }
    
	//查詢授權額度
    function allowance(address _owner, address _spender) public view returns (uint256 remaining) {
        return allowed[_owner][_spender];
    }
	
	//查詢智能合約ETH餘額
	function contractETH() public view returns (uint256 bnumber) {
      return address(this).balance;
    }
	
	function icoEnding() public onlyOwner {
      owner.transfer(address(this).balance);
    }
}