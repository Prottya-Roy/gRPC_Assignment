package services;

import com.assignment.grpc.User;
import com.assignment.grpc.userGrpc;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.logging.Logger;

public class UserService extends userGrpc.userImplBase{
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    ArrayList<RegisterUser> userArrayList = new ArrayList<RegisterUser>();

    @Override
    public void login(User.LoginRequest request, StreamObserver<User.Response> responseObserver) {
        String username = request.getUsername();
        String password = passwordHasher(request.getPassword());

        boolean loginSuccess = false;

        logger.info("Login credentials given by "+username);

        User.Response.Builder response = User.Response.newBuilder();

        for(RegisterUser registerUser : userArrayList){
            if(registerUser.registerUsername.equals(username) && registerUser.registerPassword.equals(password)){
                response.setResponseCode(200).setMessage("OK! Login Successful");

                logger.info("Login Successful for the user "+username);
                loginSuccess= true;
                break;
            }
        }
        if(!loginSuccess){
            response.setResponseCode(400).setMessage("Bad Request !!!");
            logger.info("Login failed for the user "+username);
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void register(User.RegistrationRequest request, StreamObserver<User.RegistrationResponse> responseObserver) {

        String pass1 = passwordHasher(request.getPassword());
        String reg1 = request.getUsername();
        boolean flag = true;


        for(RegisterUser registerUser : userArrayList){
            if(registerUser.registerUsername.equals(reg1)){
                flag = false;
                break;
            }
        }

        User.RegistrationResponse.Builder registrationResponse = User.RegistrationResponse.newBuilder();

        if(flag){
            RegisterUser registerUser = new RegisterUser(reg1,pass1);
            userArrayList.add(registerUser);
            registrationResponse.setResponseCode(201).setMessage("A new user by the username "+reg1+" was registered...");
        }

        else{
            registrationResponse.setResponseCode(400).setMessage("Username is already in use. please enter a different username...");
        }
        responseObserver.onNext(registrationResponse.build());
        responseObserver.onCompleted();

    }

    @Override
    public void logout(User.LogoutRequest request, StreamObserver<User.Response> responseObserver) {
        super.logout(request, responseObserver);
    }

    static String passwordHasher(String password){
        char[] string = password.toCharArray();
        String hash="";
        for (int i=0;i< password.length();i+=2){
            hash += (char)(string[i]+(i%30));
        }

        for(int i=1;i<password.length();i+=2){
            hash+=(char)(string[i]+(i%35));
        }

        return hash;
    }
}

class RegisterUser{
    String registerUsername;
    String registerPassword;

    RegisterUser(String registerUsername,String registerPassword){
        this.registerUsername= registerUsername;
        this.registerPassword = registerPassword;
    }
}
