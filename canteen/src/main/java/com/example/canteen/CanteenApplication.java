package com.example.canteen;

import com.example.canteen.database.DatabaseConnection;
import com.example.canteen.database.FoodTable;
import com.example.canteen.database.OrderTable;
import com.example.canteen.model.OrderEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CanteenApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanteenApplication.class, args);
//		final DatabaseConnection connection = new DatabaseConnection();
//		OrderTable.createOrderTable((connection.getDdbClient()));
	}
}
