pragma solidity 0.4.25;

contract TimeContract { 
 
 //取得區塊鏈時間
 function getBlockTime() public view returns (uint t) { 
   t = block.timestamp;
 } 
}
