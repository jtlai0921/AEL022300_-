pragma solidity 0.4.25;

contract fun2Contr { 
 
 event recEvn(address indexed _from, fixed128x80[2] a);
 
 function fun2(fixed128x80[2] a) public { 
  emit recEvn(msg.sender, a);
 } 
}
