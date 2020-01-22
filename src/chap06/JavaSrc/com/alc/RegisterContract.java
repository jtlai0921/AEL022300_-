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
import org.web3j.abi.datatypes.Utf8String;
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
public class RegisterContract extends Contract {
    private static final String BINARY = "{\r\n"
            + "    \"linkReferences\": {},\r\n"
            + "    \"object\": \"608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000600181905550610880806100686000396000f300608060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806334bb7ed41461007d5780639c6ef8be14610139578063b2f77874146101d6578063bde61cee14610243578063e8a2f0931461029e578063f437bc59146102c9575b600080fd5b34801561008957600080fd5b506100be600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610320565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100fe5780820151818401526020810190506100e3565b50505050905090810190601f16801561012b5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561014557600080fd5b506101c0600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506103d0565b6040518082815260200191505060405180910390f35b3480156101e257600080fd5b50610201600480360381019080803590602001909291905050506106e1565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561024f57600080fd5b50610284600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610714565b604051808215151515815260200191505060405180910390f35b3480156102aa57600080fd5b506102b3610784565b6040518082815260200191505060405180910390f35b3480156102d557600080fd5b506102de61078a565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b60036020528060005260406000206000915090508054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103c85780601f1061039d576101008083540402835291602001916103c8565b820191906000526020600020905b8154815290600101906020018083116103ab57829003601f168201915b505050505081565b60008082511415151561044b576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601b8152602001807f636f6e7472616374206e616d652063616e206e6f7420656d707479000000000081525060200191505060405180910390fd5b6000600360008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020805460018160011615610100020316600290049050141515610516576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f636f6e7472616374206164647265737320616c7265647920726567697374727981525060200191505060405180910390fd5b6001600081548092919060010191905055508260026000600154815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081600360008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002090805190602001906105cf9291906107af565b5060405180807f52656745766e0000000000000000000000000000000000000000000000000000815250600601905060405180910390207f984ae33915d061175b2f5fad07c5d6ebddb903bfde0e22d5c05f29530d867c4d8484604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561069b578082015181840152602081019050610680565b50505050905090810190601f1680156106c85780820380516001836020036101000a031916815260200191505b50935050505060405180910390a2600154905092915050565b60026020528060005260406000206000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600080600360008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020805460018160011615610100020316600290049050141561077a576000905061077f565b600190505b919050565b60015481565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106107f057805160ff191683800117855561081e565b8280016001018555821561081e579182015b8281111561081d578251825591602001919060010190610802565b5b50905061082b919061082f565b5090565b61085191905b8082111561084d576000816000905550600101610835565b5090565b905600a165627a7a72305820ff330d5419ec3100457491c13081b5451a80a0c62712a202d62438849c0269e40029\",\r\n"
            + "    \"opcodes\": \"PUSH1 0x80 PUSH1 0x40 MSTORE CALLVALUE DUP1 ISZERO PUSH2 0x10 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP CALLER PUSH1 0x0 DUP1 PUSH2 0x100 EXP DUP2 SLOAD DUP2 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF MUL NOT AND SWAP1 DUP4 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND MUL OR SWAP1 SSTORE POP PUSH1 0x0 PUSH1 0x1 DUP2 SWAP1 SSTORE POP PUSH2 0x880 DUP1 PUSH2 0x68 PUSH1 0x0 CODECOPY PUSH1 0x0 RETURN STOP PUSH1 0x80 PUSH1 0x40 MSTORE PUSH1 0x4 CALLDATASIZE LT PUSH2 0x78 JUMPI PUSH1 0x0 CALLDATALOAD PUSH29 0x100000000000000000000000000000000000000000000000000000000 SWAP1 DIV PUSH4 0xFFFFFFFF AND DUP1 PUSH4 0x34BB7ED4 EQ PUSH2 0x7D JUMPI DUP1 PUSH4 0x9C6EF8BE EQ PUSH2 0x139 JUMPI DUP1 PUSH4 0xB2F77874 EQ PUSH2 0x1D6 JUMPI DUP1 PUSH4 0xBDE61CEE EQ PUSH2 0x243 JUMPI DUP1 PUSH4 0xE8A2F093 EQ PUSH2 0x29E JUMPI DUP1 PUSH4 0xF437BC59 EQ PUSH2 0x2C9 JUMPI JUMPDEST PUSH1 0x0 DUP1 REVERT JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x89 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0xBE PUSH1 0x4 DUP1 CALLDATASIZE SUB DUP2 ADD SWAP1 DUP1 DUP1 CALLDATALOAD PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND SWAP1 PUSH1 0x20 ADD SWAP1 SWAP3 SWAP2 SWAP1 POP POP POP PUSH2 0x320 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP1 PUSH1 0x20 ADD DUP3 DUP2 SUB DUP3 MSTORE DUP4 DUP2 DUP2 MLOAD DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP DUP1 MLOAD SWAP1 PUSH1 0x20 ADD SWAP1 DUP1 DUP4 DUP4 PUSH1 0x0 JUMPDEST DUP4 DUP2 LT ISZERO PUSH2 0xFE JUMPI DUP1 DUP3 ADD MLOAD DUP2 DUP5 ADD MSTORE PUSH1 0x20 DUP2 ADD SWAP1 POP PUSH2 0xE3 JUMP JUMPDEST POP POP POP POP SWAP1 POP SWAP1 DUP2 ADD SWAP1 PUSH1 0x1F AND DUP1 ISZERO PUSH2 0x12B JUMPI DUP1 DUP3 SUB DUP1 MLOAD PUSH1 0x1 DUP4 PUSH1 0x20 SUB PUSH2 0x100 EXP SUB NOT AND DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP JUMPDEST POP SWAP3 POP POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x145 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x1C0 PUSH1 0x4 DUP1 CALLDATASIZE SUB DUP2 ADD SWAP1 DUP1 DUP1 CALLDATALOAD PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND SWAP1 PUSH1 0x20 ADD SWAP1 SWAP3 SWAP2 SWAP1 DUP1 CALLDATALOAD SWAP1 PUSH1 0x20 ADD SWAP1 DUP3 ADD DUP1 CALLDATALOAD SWAP1 PUSH1 0x20 ADD SWAP1 DUP1 DUP1 PUSH1 0x1F ADD PUSH1 0x20 DUP1 SWAP2 DIV MUL PUSH1 0x20 ADD PUSH1 0x40 MLOAD SWAP1 DUP2 ADD PUSH1 0x40 MSTORE DUP1 SWAP4 SWAP3 SWAP2 SWAP1 DUP2 DUP2 MSTORE PUSH1 0x20 ADD DUP4 DUP4 DUP1 DUP3 DUP5 CALLDATACOPY DUP3 ADD SWAP2 POP POP POP POP POP POP SWAP2 SWAP3 SWAP2 SWAP3 SWAP1 POP POP POP PUSH2 0x3D0 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP3 DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x1E2 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x201 PUSH1 0x4 DUP1 CALLDATASIZE SUB DUP2 ADD SWAP1 DUP1 DUP1 CALLDATALOAD SWAP1 PUSH1 0x20 ADD SWAP1 SWAP3 SWAP2 SWAP1 POP POP POP PUSH2 0x6E1 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP3 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x24F JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x284 PUSH1 0x4 DUP1 CALLDATASIZE SUB DUP2 ADD SWAP1 DUP1 DUP1 CALLDATALOAD PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND SWAP1 PUSH1 0x20 ADD SWAP1 SWAP3 SWAP2 SWAP1 POP POP POP PUSH2 0x714 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP3 ISZERO ISZERO ISZERO ISZERO DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x2AA JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x2B3 PUSH2 0x784 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP3 DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST CALLVALUE DUP1 ISZERO PUSH2 0x2D5 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x2DE PUSH2 0x78A JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 DUP3 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST PUSH1 0x3 PUSH1 0x20 MSTORE DUP1 PUSH1 0x0 MSTORE PUSH1 0x40 PUSH1 0x0 KECCAK256 PUSH1 0x0 SWAP2 POP SWAP1 POP DUP1 SLOAD PUSH1 0x1 DUP2 PUSH1 0x1 AND ISZERO PUSH2 0x100 MUL SUB AND PUSH1 0x2 SWAP1 DIV DUP1 PUSH1 0x1F ADD PUSH1 0x20 DUP1 SWAP2 DIV MUL PUSH1 0x20 ADD PUSH1 0x40 MLOAD SWAP1 DUP2 ADD PUSH1 0x40 MSTORE DUP1 SWAP3 SWAP2 SWAP1 DUP2 DUP2 MSTORE PUSH1 0x20 ADD DUP3 DUP1 SLOAD PUSH1 0x1 DUP2 PUSH1 0x1 AND ISZERO PUSH2 0x100 MUL SUB AND PUSH1 0x2 SWAP1 DIV DUP1 ISZERO PUSH2 0x3C8 JUMPI DUP1 PUSH1 0x1F LT PUSH2 0x39D JUMPI PUSH2 0x100 DUP1 DUP4 SLOAD DIV MUL DUP4 MSTORE SWAP2 PUSH1 0x20 ADD SWAP2 PUSH2 0x3C8 JUMP JUMPDEST DUP3 ADD SWAP2 SWAP1 PUSH1 0x0 MSTORE PUSH1 0x20 PUSH1 0x0 KECCAK256 SWAP1 JUMPDEST DUP2 SLOAD DUP2 MSTORE SWAP1 PUSH1 0x1 ADD SWAP1 PUSH1 0x20 ADD DUP1 DUP4 GT PUSH2 0x3AB JUMPI DUP3 SWAP1 SUB PUSH1 0x1F AND DUP3 ADD SWAP2 JUMPDEST POP POP POP POP POP DUP2 JUMP JUMPDEST PUSH1 0x0 DUP1 DUP3 MLOAD EQ ISZERO ISZERO ISZERO PUSH2 0x44B JUMPI PUSH1 0x40 MLOAD PUSH32 0x8C379A000000000000000000000000000000000000000000000000000000000 DUP2 MSTORE PUSH1 0x4 ADD DUP1 DUP1 PUSH1 0x20 ADD DUP3 DUP2 SUB DUP3 MSTORE PUSH1 0x1B DUP2 MSTORE PUSH1 0x20 ADD DUP1 PUSH32 0x636F6E7472616374206E616D652063616E206E6F7420656D7074790000000000 DUP2 MSTORE POP PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 REVERT JUMPDEST PUSH1 0x0 PUSH1 0x3 PUSH1 0x0 DUP6 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 MSTORE PUSH1 0x20 ADD SWAP1 DUP2 MSTORE PUSH1 0x20 ADD PUSH1 0x0 KECCAK256 DUP1 SLOAD PUSH1 0x1 DUP2 PUSH1 0x1 AND ISZERO PUSH2 0x100 MUL SUB AND PUSH1 0x2 SWAP1 DIV SWAP1 POP EQ ISZERO ISZERO PUSH2 0x516 JUMPI PUSH1 0x40 MLOAD PUSH32 0x8C379A000000000000000000000000000000000000000000000000000000000 DUP2 MSTORE PUSH1 0x4 ADD DUP1 DUP1 PUSH1 0x20 ADD DUP3 DUP2 SUB DUP3 MSTORE PUSH1 0x20 DUP2 MSTORE PUSH1 0x20 ADD DUP1 PUSH32 0x636F6E7472616374206164647265737320616C72656479207265676973747279 DUP2 MSTORE POP PUSH1 0x20 ADD SWAP2 POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 REVERT JUMPDEST PUSH1 0x1 PUSH1 0x0 DUP2 SLOAD DUP1 SWAP3 SWAP2 SWAP1 PUSH1 0x1 ADD SWAP2 SWAP1 POP SSTORE POP DUP3 PUSH1 0x2 PUSH1 0x0 PUSH1 0x1 SLOAD DUP2 MSTORE PUSH1 0x20 ADD SWAP1 DUP2 MSTORE PUSH1 0x20 ADD PUSH1 0x0 KECCAK256 PUSH1 0x0 PUSH2 0x100 EXP DUP2 SLOAD DUP2 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF MUL NOT AND SWAP1 DUP4 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND MUL OR SWAP1 SSTORE POP DUP2 PUSH1 0x3 PUSH1 0x0 DUP6 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 MSTORE PUSH1 0x20 ADD SWAP1 DUP2 MSTORE PUSH1 0x20 ADD PUSH1 0x0 KECCAK256 SWAP1 DUP1 MLOAD SWAP1 PUSH1 0x20 ADD SWAP1 PUSH2 0x5CF SWAP3 SWAP2 SWAP1 PUSH2 0x7AF JUMP JUMPDEST POP PUSH1 0x40 MLOAD DUP1 DUP1 PUSH32 0x52656745766E0000000000000000000000000000000000000000000000000000 DUP2 MSTORE POP PUSH1 0x6 ADD SWAP1 POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 KECCAK256 PUSH32 0x984AE33915D061175B2F5FAD07C5D6EBDDB903BFDE0E22D5C05F29530D867C4D DUP5 DUP5 PUSH1 0x40 MLOAD DUP1 DUP4 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 MSTORE PUSH1 0x20 ADD DUP1 PUSH1 0x20 ADD DUP3 DUP2 SUB DUP3 MSTORE DUP4 DUP2 DUP2 MLOAD DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP DUP1 MLOAD SWAP1 PUSH1 0x20 ADD SWAP1 DUP1 DUP4 DUP4 PUSH1 0x0 JUMPDEST DUP4 DUP2 LT ISZERO PUSH2 0x69B JUMPI DUP1 DUP3 ADD MLOAD DUP2 DUP5 ADD MSTORE PUSH1 0x20 DUP2 ADD SWAP1 POP PUSH2 0x680 JUMP JUMPDEST POP POP POP POP SWAP1 POP SWAP1 DUP2 ADD SWAP1 PUSH1 0x1F AND DUP1 ISZERO PUSH2 0x6C8 JUMPI DUP1 DUP3 SUB DUP1 MLOAD PUSH1 0x1 DUP4 PUSH1 0x20 SUB PUSH2 0x100 EXP SUB NOT AND DUP2 MSTORE PUSH1 0x20 ADD SWAP2 POP JUMPDEST POP SWAP4 POP POP POP POP PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 LOG2 PUSH1 0x1 SLOAD SWAP1 POP SWAP3 SWAP2 POP POP JUMP JUMPDEST PUSH1 0x2 PUSH1 0x20 MSTORE DUP1 PUSH1 0x0 MSTORE PUSH1 0x40 PUSH1 0x0 KECCAK256 PUSH1 0x0 SWAP2 POP SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 JUMP JUMPDEST PUSH1 0x0 DUP1 PUSH1 0x3 PUSH1 0x0 DUP5 PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 MSTORE PUSH1 0x20 ADD SWAP1 DUP2 MSTORE PUSH1 0x20 ADD PUSH1 0x0 KECCAK256 DUP1 SLOAD PUSH1 0x1 DUP2 PUSH1 0x1 AND ISZERO PUSH2 0x100 MUL SUB AND PUSH1 0x2 SWAP1 DIV SWAP1 POP EQ ISZERO PUSH2 0x77A JUMPI PUSH1 0x0 SWAP1 POP PUSH2 0x77F JUMP JUMPDEST PUSH1 0x1 SWAP1 POP JUMPDEST SWAP2 SWAP1 POP JUMP JUMPDEST PUSH1 0x1 SLOAD DUP2 JUMP JUMPDEST PUSH1 0x0 DUP1 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH20 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF AND DUP2 JUMP JUMPDEST DUP3 DUP1 SLOAD PUSH1 0x1 DUP2 PUSH1 0x1 AND ISZERO PUSH2 0x100 MUL SUB AND PUSH1 0x2 SWAP1 DIV SWAP1 PUSH1 0x0 MSTORE PUSH1 0x20 PUSH1 0x0 KECCAK256 SWAP1 PUSH1 0x1F ADD PUSH1 0x20 SWAP1 DIV DUP2 ADD SWAP3 DUP3 PUSH1 0x1F LT PUSH2 0x7F0 JUMPI DUP1 MLOAD PUSH1 0xFF NOT AND DUP4 DUP1 ADD OR DUP6 SSTORE PUSH2 0x81E JUMP JUMPDEST DUP3 DUP1 ADD PUSH1 0x1 ADD DUP6 SSTORE DUP3 ISZERO PUSH2 0x81E JUMPI SWAP2 DUP3 ADD JUMPDEST DUP3 DUP2 GT ISZERO PUSH2 0x81D JUMPI DUP3 MLOAD DUP3 SSTORE SWAP2 PUSH1 0x20 ADD SWAP2 SWAP1 PUSH1 0x1 ADD SWAP1 PUSH2 0x802 JUMP JUMPDEST JUMPDEST POP SWAP1 POP PUSH2 0x82B SWAP2 SWAP1 PUSH2 0x82F JUMP JUMPDEST POP SWAP1 JUMP JUMPDEST PUSH2 0x851 SWAP2 SWAP1 JUMPDEST DUP1 DUP3 GT ISZERO PUSH2 0x84D JUMPI PUSH1 0x0 DUP2 PUSH1 0x0 SWAP1 SSTORE POP PUSH1 0x1 ADD PUSH2 0x835 JUMP JUMPDEST POP SWAP1 JUMP JUMPDEST SWAP1 JUMP STOP LOG1 PUSH6 0x627A7A723058 KECCAK256 SELFDESTRUCT CALLER 0xd SLOAD NOT 0xec BALANCE STOP GASLIMIT PUSH21 0x91C13081B5451A80A0C62712A202D62438849C0269 0xe4 STOP 0x29 \",\r\n"
            + "    \"sourceMap\": \"28:1579:0:-;;;503:71;8:9:-1;5:2;;;30:1;27;20:12;5:2;503:71:0;538:10;531:4;;:17;;;;;;;;;;;;;;;;;;568:1;554:11;:15;;;;28:1579;;;;;;\"\r\n"
            + "}";

    public static final String FUNC_ADDRTONAMEMAPPING = "addrToNameMapping";

    public static final String FUNC_REGCONTRACT = "regContract";

    public static final String FUNC_IDTOADDRMAPPING = "idToAddrMapping";

    public static final String FUNC_ISCONTRACTEXIST = "isContractExist";

    public static final String FUNC_CONTRACTCNT = "contractCnt";

    public static final String FUNC_HOST = "host";

    public static final Event REGEVT_EVENT = new Event("RegEvt", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>(true) {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected RegisterContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected RegisterContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<String> addrToNameMapping(String param0) {
        final Function function = new Function(FUNC_ADDRTONAMEMAPPING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> regContract(String contractAddr, String contractName) {
        final Function function = new Function(
                FUNC_REGCONTRACT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(contractAddr), 
                new org.web3j.abi.datatypes.Utf8String(contractName)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> idToAddrMapping(BigInteger param0) {
        final Function function = new Function(FUNC_IDTOADDRMAPPING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> isContractExist(String contractAddr) {
        final Function function = new Function(FUNC_ISCONTRACTEXIST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(contractAddr)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> contractCnt() {
        final Function function = new Function(FUNC_CONTRACTCNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> host() {
        final Function function = new Function(FUNC_HOST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<RegisterContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(RegisterContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<RegisterContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(RegisterContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public List<RegEvtEventResponse> getRegEvtEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(REGEVT_EVENT, transactionReceipt);
        ArrayList<RegEvtEventResponse> responses = new ArrayList<RegEvtEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RegEvtEventResponse typedResponse = new RegEvtEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.eventType = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.contractAddr = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.contractName = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<RegEvtEventResponse> regEvtEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, RegEvtEventResponse>() {
            @Override
            public RegEvtEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(REGEVT_EVENT, log);
                RegEvtEventResponse typedResponse = new RegEvtEventResponse();
                typedResponse.log = log;
                typedResponse.eventType = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.contractAddr = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.contractName = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<RegEvtEventResponse> regEvtEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REGEVT_EVENT));
        return regEvtEventObservable(filter);
    }

    public static RegisterContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new RegisterContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static RegisterContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new RegisterContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class RegEvtEventResponse {
        public Log log;

        public byte[] eventType;

        public String contractAddr;

        public String contractName;
    }
}