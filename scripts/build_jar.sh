cd client/src/main/java

javac -cp ".:../../../../lib/*" com/sendwithus/exception/SendWithUsException.java

javac -cp ".:../../../../lib/*" com/sendwithus/model/DeactivatedDrips.java
javac -cp ".:../../../../lib/*" com/sendwithus/model/Email.java
javac -cp ".:../../../../lib/*" com/sendwithus/model/SendReceipt.java

javac -cp ".:../../../../lib/*" com/sendwithus/SendWithUs.java

jar cvf sendwithus-dev.jar ./
cd ../../../../

mv client/src/main/java/sendwithus-dev.jar ./
