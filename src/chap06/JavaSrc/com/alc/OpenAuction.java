package chap06.com.alc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.5.0.
 */
public class OpenAuction extends Contract {
    private static final String BINARY = "{\r\n"
            + "\t\"linkReferences\": {},\r\n"
            + "\t\"object\": \"6080604052600560005534801561001557600080fd5b50610a98806100256000396000f3006080604052600436106100a4576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063181f44561461047957806338af3eed146104a657806344514a42146104fd5780634f245ef71461052857806365c43cc714610553578063807029911461057e57806391f9015714610595578063b103a5d3146105ec578063c70a0bf31461061b578063d57bde791461064a575b7fae23f9a2a60a4cc53dc02793add82204ace892824ccbf047bbf5bb01b046a2343334604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a1600660009054906101000a900460ff161515610193576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260158152602001807f61756374696f6e206e6f7420796574207374617274000000000000000000000081525060200191505060405180910390fd5b3373ffffffffffffffffffffffffffffffffffffffff16600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151515610259576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260168152602001807f596f75206172652068696768657374206269646465720000000000000000000081525060200191505060405180910390fd5b60025442111515156102d3576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600d8152602001807f61756374696f6e20656e6465640000000000000000000000000000000000000081525060200191505060405180910390fd5b6005543411151561034c576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260158152602001807f6c657373207468616e206869676865737420626964000000000000000000000081525060200191505060405180910390fd5b60006005541415156103c457600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc6005549081150290604051600060405180830381858888f193505050501580156103c2573d6000803e3d6000fd5b505b33600460006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550346005819055507f966ad1a6178f075eaa56ad0c93d5eaaf0c7bccd0698de8df4e1f43cf008147b23334604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a1005b34801561048557600080fd5b506104a460048036038101908080359060200190929190505050610675565b005b3480156104b257600080fd5b506104bb6107cc565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561050957600080fd5b506105126107f2565b6040518082815260200191505060405180910390f35b34801561053457600080fd5b5061053d6107f8565b6040518082815260200191505060405180910390f35b34801561055f57600080fd5b506105686107fe565b6040518082815260200191505060405180910390f35b34801561058a57600080fd5b50610593610804565b005b3480156105a157600080fd5b506105aa610a1a565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156105f857600080fd5b50610601610a40565b604051808215151515815260200191505060405180910390f35b34801561062757600080fd5b50610630610a53565b604051808215151515815260200191505060405180910390f35b34801561065657600080fd5b5061065f610a66565b6040518082815260200191505060405180910390f35b600660009054906101000a900460ff161515156106fa576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260158152602001807f61756374696f6e20616c7265616479207374617274000000000000000000000081525060200191505060405180910390fd5b33600360006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550426001819055508042016002819055506001600660006101000a81548160ff0219169083151502179055507f6c3719454c89325fe29e3846d6b988df729d93a6768582165a00abbf39ecbe6233604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a150565b600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60025481565b60015481565b60005481565b600254421015151561087e576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260158152602001807f61756374696f6e206e6f742079657420656e646564000000000000000000000081525060200191505060405180910390fd5b600660019054906101000a900460ff16151515610903576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260158152602001807f61756374696f6e20616c726561647920656e646564000000000000000000000081525060200191505060405180910390fd5b6001600660016101000a81548160ff0219169083151502179055507fe36fae04acd4f7bfaf8fa687c17875bdbc2879c555392ee79a97fbf5c65595d7600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600554604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a1600360009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc6005549081150290604051600060405180830381858888f19350505050158015610a17573d6000803e3d6000fd5b50565b600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600660009054906101000a900460ff1681565b600660019054906101000a900460ff1681565b600554815600a165627a7a723058202b1ab24b284b787d4a06fec26a5b2ca4e3fe5af16beb3866719ebffbdd2c2bc50029\",\r\n"
            + "\t\"opcodes\": \"PUSH1 0x80 PUSH1 0x40 MSTORE PUSH1 0x5 PUSH1 0x0 SSTORE CALLVALUE DUP1 ISZERO PUSH2 0x15 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0xA98 DUP1 PUSH2 0x25 PUSH1 0x0 CODECOPY PUSH1 0x0 RETURN STOP PUSH1 0x80 PUSH1 0x40 MSTORE PUSH1 0x4 CALLDATASIZE LT PUSH2 0xA4 JUMPI PUSH1 0x0 CALLDATALOAD PUSH29 0x100000000000000000000000000000000000000000000000000000000 SWAP1 DIV PUSH4 0xFFFFFFFF AND DUP1 PUSH4 0x181F4456 EQ PUSH2 0x479 JUMPI DUP1 PUSH4 0x38AF3EED EQ PUSH2 0x4A6 JUMPI DUP1 PUSH4 0x44514A42 EQ PUSH2 0x4FD JUMPI DUP1 PUSH4 0x4F245EF7 EQ PUSH2 0x528 JUMPI DUP1 PUSH4 0x65C43CC7 EQ PUSH2 0x553 JUMPI DUP1 PUSH4 0x80702991 EQ PUSH2 0x57E JUMPI DUP1 PUSH4 0x91F90157 EQ PUSH2 0x595 JUMPI DUP1 PUSH4 0xB103A5D3 EQ PUSH2 0x5EC JUMPI DUP1 PUSH4 0xC70A0BF3 EQ PUSH2 0x61B JUMPI DUP1 PUSH4 0xD57BDE79 EQ PUSH2 0x64A JUMPI JUMPDEST PUSH32 0xAE23F9A2A60A4CC53DC02793ADD82204ACE892824CCBF047BBF5BB01B046A234 CALLER CALLVALUE PUSH1 0x40 MLOAD DUP1 DUP4 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 MSTORE PUSH1 0x20 ADD DUP3 DUP2 MSTORE PUSH1 0x20 ADD SWAP3 POP POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 LOG1 PUSH1 0x6 PUSH1 0x0 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH1 0xFF AND ISZERO ISZERO PUSH2 0x193 JUMPI PUSH1 0x40 MLOAD PUSH32 0x8C379A000000000000000000000000000000000000000000000000000000000 DUP2 MSTORE PUSH1 0x4 ADD DUP1 DUP1 PUSH1 0x20 ADD DUP3 DUP2 SUB DUP3 MSTORE PUSH1 0x15 DUP2 MSTORE PUSH1 0x20 ADD DUP1 PUSH32 0x61756374696F6E206E6F74207965742073746172740000000000000000000000 DUP2 MSTORE POP PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 REVERT JUMPDEST CALLER PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH1 0x4 PUSH1 0x0 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND EQ ISZERO ISZERO ISZERO PUSH2 0x259 JUMPI PUSH1 0x40 MLOAD PUSH32 0x8C379A000000000000000000000000000000000000000000000000000000000 DUP2 MSTORE PUSH1 0x4 ADD DUP1 DUP1 PUSH1 0x20 ADD DUP3 DUP2 SUB DUP3 MSTORE PUSH1 0x16 DUP2 MSTORE PUSH1 0x20 ADD DUP1 PUSH32 0x596F752061726520686967686573742062696464657200000000000000000000 DUP2 MSTORE POP PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 REVERT JUMPDEST PUSH1 0x2 SLOAD TIMESTAMP GT ISZERO ISZERO ISZERO PUSH2 0x2D3 JUMPI PUSH1 0x40 MLOAD PUSH32 0x8C379A000000000000000000000000000000000000000000000000000000000 DUP2 MSTORE PUSH1 0x4 ADD DUP1 DUP1 PUSH1 0x20 ADD DUP3 DUP2 SUB DUP3 MSTORE PUSH1 0xD DUP2 MSTORE PUSH1 0x20 ADD DUP1 PUSH32 0x61756374696F6E20656E64656400000000000000000000000000000000000000 DUP2 MSTORE POP PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 REVERT JUMPDEST PUSH1 0x5 SLOAD CALLVALUE GT ISZERO ISZERO PUSH2 0x34C JUMPI PUSH1 0x40 MLOAD PUSH32 0x8C379A000000000000000000000000000000000000000000000000000000000 DUP2 MSTORE PUSH1 0x4 ADD DUP1 DUP1 PUSH1 0x20 ADD DUP3 DUP2 SUB DUP3 MSTORE PUSH1 0x15 DUP2 MSTORE PUSH1 0x20 ADD DUP1 PUSH32 0x6C657373207468616E2068696768657374206269640000000000000000000000 DUP2 MSTORE POP PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 REVERT JUMPDEST PUSH1 0x0 PUSH1 0x5 SLOAD EQ ISZERO ISZERO PUSH2 0x3C4 JUMPI PUSH1 0x4 PUSH1 0x0 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH2 0x8FC PUSH1 0x5 SLOAD SWAP1 DUP2 ISZERO MUL SWAP1 PUSH1 0x40 MLOAD PUSH1 0x0 PUSH1 0x40 MLOAD DUP1 DUP4 SUB DUP2 DUP6 DUP9 DUP9 CALL SWAP4 POP POP POP POP ISZERO DUP1 ISZERO PUSH2 0x3C2 JUMPI RETURNDATASIZE PUSH1 0x0 DUP1 RETURNDATACOPY RETURNDATASIZE PUSH1 0x0 REVERT JUMPDEST POP JUMPDEST CALLER PUSH1 0x4 PUSH1 0x0 PUSH2 0x100 EXP DUP2 SLOAD DUP2 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF MUL NOT AND SWAP1 DUP4 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND MUL OR SWAP1 SSTORE POP CALLVALUE PUSH1 0x5 DUP2 SWAP1 SSTORE POP PUSH32 0x966AD1A6178F075EAA56AD0C93D5EAAF0C7BCCD0698DE8DF4E1F43CF008147B2 CALLER CALLVALUE PUSH1 0x40 MLOAD DUP1 DUP4 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 MSTORE PUSH1 0x20 ADD DUP3 DUP2 MSTORE PUSH1 0x20 ADD SWAP3 POP POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 LOG1 STOP JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x485 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x4A4 PUSH1 0x4 DUP1 CALLDATASIZE SUB DUP2 ADD SWAP1 DUP1 DUP1 CALLDATALOAD SWAP1 PUSH1 0x20 ADD SWAP1 SWAP3 SWAP2 SWAP1 POP POP POP PUSH2 0x675 JUMP JUMPDEST STOP JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x4B2 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x4BB PUSH2 0x7CC JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP3 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x509 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x512 PUSH2 0x7F2 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP3 DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x534 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x53D PUSH2 0x7F8 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP3 DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x55F JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x568 PUSH2 0x7FE JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP3 DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x58A JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x593 PUSH2 0x804 JUMP JUMPDEST STOP JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x5A1 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x5AA PUSH2 0xA1A JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP3 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x5F8 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x601 PUSH2 0xA40 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP3 ISZERO ISZERO ISZERO ISZERO DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x627 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x630 PUSH2 0xA53 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP3 ISZERO ISZERO ISZERO ISZERO DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x656 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x65F PUSH2 0xA66 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP3 DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST PUSH1 0x6 PUSH1 0x0 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH1 0xFF AND ISZERO ISZERO ISZERO PUSH2 0x6FA JUMPI PUSH1 0x40 MLOAD PUSH32 0x8C379A000000000000000000000000000000000000000000000000000000000 DUP2 MSTORE PUSH1 0x4 ADD DUP1 DUP1 PUSH1 0x20 ADD DUP3 DUP2 SUB DUP3 MSTORE PUSH1 0x15 DUP2 MSTORE PUSH1 0x20 ADD DUP1 PUSH32 0x61756374696F6E20616C72656164792073746172740000000000000000000000 DUP2 MSTORE POP PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 REVERT JUMPDEST CALLER PUSH1 0x3 PUSH1 0x0 PUSH2 0x100 EXP DUP2 SLOAD DUP2 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF MUL NOT AND SWAP1 DUP4 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND MUL OR SWAP1 SSTORE POP TIMESTAMP PUSH1 0x1 DUP2 SWAP1 SSTORE POP DUP1 TIMESTAMP ADD PUSH1 0x2 DUP2 SWAP1 SSTORE POP PUSH1 0x1 PUSH1 0x6 PUSH1 0x0 PUSH2 0x100 EXP DUP2 SLOAD DUP2 PUSH1 0xFF MUL NOT AND SWAP1 DUP4 ISZERO ISZERO MUL OR SWAP1 SSTORE POP PUSH32 0x6C3719454C89325FE29E3846D6B988DF729D93A6768582165A00ABBF39ECBE62 CALLER PUSH1 0x40 MLOAD DUP1 DUP3 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 LOG1 POP JUMP JUMPDEST PUSH1 0x3 PUSH1 0x0 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 JUMP JUMPDEST PUSH1 0x2 SLOAD DUP2 JUMP JUMPDEST PUSH1 0x1 SLOAD DUP2 JUMP JUMPDEST PUSH1 0x0 SLOAD DUP2 JUMP JUMPDEST PUSH1 0x2 SLOAD TIMESTAMP LT ISZERO ISZERO ISZERO PUSH2 0x87E JUMPI PUSH1 0x40 MLOAD PUSH32 0x8C379A000000000000000000000000000000000000000000000000000000000 DUP2 MSTORE PUSH1 0x4 ADD DUP1 DUP1 PUSH1 0x20 ADD DUP3 DUP2 SUB DUP3 MSTORE PUSH1 0x15 DUP2 MSTORE PUSH1 0x20 ADD DUP1 PUSH32 0x61756374696F6E206E6F742079657420656E6465640000000000000000000000 DUP2 MSTORE POP PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 REVERT JUMPDEST PUSH1 0x6 PUSH1 0x1 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH1 0xFF AND ISZERO ISZERO ISZERO PUSH2 0x903 JUMPI PUSH1 0x40 MLOAD PUSH32 0x8C379A000000000000000000000000000000000000000000000000000000000 DUP2 MSTORE PUSH1 0x4 ADD DUP1 DUP1 PUSH1 0x20 ADD DUP3 DUP2 SUB DUP3 MSTORE PUSH1 0x15 DUP2 MSTORE PUSH1 0x20 ADD DUP1 PUSH32 0x61756374696F6E20616C726561647920656E6465640000000000000000000000 DUP2 MSTORE POP PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 REVERT JUMPDEST PUSH1 0x1 PUSH1 0x6 PUSH1 0x1 PUSH2 0x100 EXP DUP2 SLOAD DUP2 PUSH1 0xFF MUL NOT AND SWAP1 DUP4 ISZERO ISZERO MUL OR SWAP1 SSTORE POP PUSH32 0xE36FAE04ACD4F7BFAF8FA687C17875BDBC2879C555392EE79A97FBF5C65595D7 PUSH1 0x4 PUSH1 0x0 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH1 0x5 SLOAD PUSH1 0x40 MLOAD DUP1 DUP4 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 MSTORE PUSH1 0x20 ADD DUP3 DUP2 MSTORE PUSH1 0x20 ADD SWAP3 POP POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 LOG1 PUSH1 0x3 PUSH1 0x0 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH2 0x8FC PUSH1 0x5 SLOAD SWAP1 DUP2 ISZERO MUL SWAP1 PUSH1 0x40 MLOAD PUSH1 0x0 PUSH1 0x40 MLOAD DUP1 DUP4 SUB DUP2 DUP6 DUP9 DUP9 CALL SWAP4 POP POP POP POP ISZERO DUP1 ISZERO PUSH2 0xA17 JUMPI RETURNDATASIZE PUSH1 0x0 DUP1 RETURNDATACOPY RETURNDATASIZE PUSH1 0x0 REVERT JUMPDEST POP JUMP JUMPDEST PUSH1 0x4 PUSH1 0x0 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 JUMP JUMPDEST PUSH1 0x6 PUSH1 0x0 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH1 0xFF AND DUP2 JUMP JUMPDEST PUSH1 0x6 PUSH1 0x1 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH1 0xFF AND DUP2 JUMP JUMPDEST PUSH1 0x5 SLOAD DUP2 JUMP STOP LOG1 PUSH6 0x627A7A723058 KECCAK256 0x2b BYTE 0xb2 0x4b 0x28 0x4b PUSH25 0x7D4A06FEC26A5B2CA4E3FE5AF16BEB3866719EBFFBDD2C2BC5 STOP 0x29 \",\r\n"
            + "\t\"sourceMap\": \"28:2711:0:-;;;93:1;77:17;;28:2711;8:9:-1;5:2;;;30:1;27;20:12;5:2;28:2711:0;;;;;;;\"\r\n"
            + "}";

    public static final String FUNC_SETAUCTIONEND = "setAuctionEnd";

    public static final String FUNC_SETAUCTIONSTART = "setAuctionStart";

    public static final String FUNC_AUCTIONLIMIT = "auctionLimit";

    public static final String FUNC_AUCTIONSTART = "auctionStart";

    public static final String FUNC_BENEFICIARY = "beneficiary";

    public static final String FUNC_ENDFLG = "endFlg";

    public static final String FUNC_HIGHESTBID = "highestBid";

    public static final String FUNC_HIGHESTBIDDER = "highestBidder";

    public static final String FUNC_STARTFLG = "startFlg";

    public static final String FUNC_VER = "ver";

    public static final Event HIGHBIDEVT_EVENT = new Event("HighBidEvt", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event AUCTIONSTARTEVT_EVENT = new Event("AuctionStartEvt", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event AUCTIONENDEDEVT_EVENT = new Event("AuctionEndedEvt", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event BIDEVT_EVENT = new Event("BidEvt", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    protected OpenAuction(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected OpenAuction(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> setAuctionEnd() {
        final Function function = new Function(
                FUNC_SETAUCTIONEND, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setAuctionStart(BigInteger _timeLimit) {
        final Function function = new Function(
                FUNC_SETAUCTIONSTART, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_timeLimit)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<HighBidEvtEventResponse> getHighBidEvtEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(HIGHBIDEVT_EVENT, transactionReceipt);
        ArrayList<HighBidEvtEventResponse> responses = new ArrayList<HighBidEvtEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            HighBidEvtEventResponse typedResponse = new HighBidEvtEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.bidder = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<HighBidEvtEventResponse> highBidEvtEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, HighBidEvtEventResponse>() {
            @Override
            public HighBidEvtEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(HIGHBIDEVT_EVENT, log);
                HighBidEvtEventResponse typedResponse = new HighBidEvtEventResponse();
                typedResponse.log = log;
                typedResponse.bidder = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<HighBidEvtEventResponse> highBidEvtEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(HIGHBIDEVT_EVENT));
        return highBidEvtEventObservable(filter);
    }

    public List<AuctionStartEvtEventResponse> getAuctionStartEvtEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(AUCTIONSTARTEVT_EVENT, transactionReceipt);
        ArrayList<AuctionStartEvtEventResponse> responses = new ArrayList<AuctionStartEvtEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AuctionStartEvtEventResponse typedResponse = new AuctionStartEvtEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.starter = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<AuctionStartEvtEventResponse> auctionStartEvtEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, AuctionStartEvtEventResponse>() {
            @Override
            public AuctionStartEvtEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(AUCTIONSTARTEVT_EVENT, log);
                AuctionStartEvtEventResponse typedResponse = new AuctionStartEvtEventResponse();
                typedResponse.log = log;
                typedResponse.starter = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<AuctionStartEvtEventResponse> auctionStartEvtEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(AUCTIONSTARTEVT_EVENT));
        return auctionStartEvtEventObservable(filter);
    }

    public List<AuctionEndedEvtEventResponse> getAuctionEndedEvtEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(AUCTIONENDEDEVT_EVENT, transactionReceipt);
        ArrayList<AuctionEndedEvtEventResponse> responses = new ArrayList<AuctionEndedEvtEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AuctionEndedEvtEventResponse typedResponse = new AuctionEndedEvtEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.winner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<AuctionEndedEvtEventResponse> auctionEndedEvtEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, AuctionEndedEvtEventResponse>() {
            @Override
            public AuctionEndedEvtEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(AUCTIONENDEDEVT_EVENT, log);
                AuctionEndedEvtEventResponse typedResponse = new AuctionEndedEvtEventResponse();
                typedResponse.log = log;
                typedResponse.winner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<AuctionEndedEvtEventResponse> auctionEndedEvtEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(AUCTIONENDEDEVT_EVENT));
        return auctionEndedEvtEventObservable(filter);
    }

    public List<BidEvtEventResponse> getBidEvtEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(BIDEVT_EVENT, transactionReceipt);
        ArrayList<BidEvtEventResponse> responses = new ArrayList<BidEvtEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BidEvtEventResponse typedResponse = new BidEvtEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.bidder = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<BidEvtEventResponse> bidEvtEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, BidEvtEventResponse>() {
            @Override
            public BidEvtEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(BIDEVT_EVENT, log);
                BidEvtEventResponse typedResponse = new BidEvtEventResponse();
                typedResponse.log = log;
                typedResponse.bidder = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<BidEvtEventResponse> bidEvtEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BIDEVT_EVENT));
        return bidEvtEventObservable(filter);
    }

    public RemoteCall<BigInteger> auctionLimit() {
        final Function function = new Function(FUNC_AUCTIONLIMIT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> auctionStart() {
        final Function function = new Function(FUNC_AUCTIONSTART, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> beneficiary() {
        final Function function = new Function(FUNC_BENEFICIARY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> endFlg() {
        final Function function = new Function(FUNC_ENDFLG, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> highestBid() {
        final Function function = new Function(FUNC_HIGHESTBID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> highestBidder() {
        final Function function = new Function(FUNC_HIGHESTBIDDER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> startFlg() {
        final Function function = new Function(FUNC_STARTFLG, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> ver() {
        final Function function = new Function(FUNC_VER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static RemoteCall<OpenAuction> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(OpenAuction.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<OpenAuction> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(OpenAuction.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static OpenAuction load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new OpenAuction(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static OpenAuction load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new OpenAuction(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class HighBidEvtEventResponse {
        public Log log;

        public String bidder;

        public BigInteger amount;
    }

    public static class AuctionStartEvtEventResponse {
        public Log log;

        public String starter;
    }

    public static class AuctionEndedEvtEventResponse {
        public Log log;

        public String winner;

        public BigInteger amount;
    }

    public static class BidEvtEventResponse {
        public Log log;

        public String bidder;

        public BigInteger amount;
    }
}
