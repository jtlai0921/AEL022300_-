pragma solidity 0.4.25;

contract case2Contr { 
 
 event recEvn(address indexed _from, bytes a, bool b, uint[] c);
 
 function fun(bytes a, bool b, uint[] c) public { 
  emit recEvn(msg.sender, a, b, c);
 } 
}
