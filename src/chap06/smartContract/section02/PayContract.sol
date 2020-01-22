pragma solidity ^0.4.25;

contract PayContract {
    
    function () public payable {
    }

	function queryBalance() public view returns (uint) {
	  return address(this).balance;
    }
    
    function transEth(address recEoa) public {
	  recEoa.transfer(address(this).balance);
    }
}
