pragma solidity 0.4.25;

contract ageContr { 
 
 //判斷成年人
 function isAdult(uint32 x, bool y) public pure returns (bool r) { 
   r = x > 20 || y; 
 } 
}
