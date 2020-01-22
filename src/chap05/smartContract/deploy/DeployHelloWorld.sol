pragma solidity 0.4.25;

contract DeployHelloWorld {
    
   string greetWord = "hello world";
   string name;

   constructor() public {
   }

   function getgreet() public view returns (string){
    bytes storage _name = bytes(name);
    bytes storage _greetWord = bytes(greetWord);
    
    string memory abcde = new string(_name.length + _greetWord.length);
    bytes memory babcde = bytes(abcde);
    
    uint k = 0;
    for (uint i = 0; i < _greetWord.length; i++) 
      babcde[k++] = _greetWord[i];
    
    for (i = 0; i < _name.length; i++) 
      babcde[k++] = _name[i];
    
    return string(babcde);
   }
}