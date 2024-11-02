import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    private String firstName;
    private String lastName;
    private String address;
    private int metroStation; // Изменено на int, если это ID станции метро
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;
}