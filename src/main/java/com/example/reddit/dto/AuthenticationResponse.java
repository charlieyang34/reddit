package com.example.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

//dto是什么：DTO (经过处理后的PO，可能增加或者减少PO的属性)
//DTO是一个普通的Java类，它封装了要传送的批量的数据。 当客户端需要读取服务器端的数据的时候，服务器端将数据封装在DTO中，
// 这样客户端就可以在一个网络调用中获得它需要的所有数据。 易于实现快速开发。
//比如我们一张表有100个字段，那么对应的PO就有100个属性。
//但是我们界面上只要显示10个字段，
//客户端用WEB service来获取数据，没有必要把整个PO对象传递到客户端，
//这时我们就可以用只有这10个属性的DTO来传递结果到客户端，这样也不会暴露服务端表结构.到达客户端以后，如果用这个对象来对应界面显示，那此时它的身份就转为VO。
//表现层与应用层之间是通过数据传输对象（DTO）进行交互的，数据传输对象是没有行为的POCO对象，
// 它的目的只是为了对领域对象进行数据封装，实现层与层之间的数据传递。
// 为何不能直接将领域对象用于数据传递？因为领域对象更注重领域，而DTO更注重数据。不仅如此，由于“富领域模型”的特点，这样 做会直接将领域对象的行为暴露给表现层。
//
//
//需要了解的是，数据传输对象DTO本身并不是业务对象。数据传输对象是根据UI的需求进行设计的，
// 而不是根据领域对象进行设计的。
// 比如，Customer领域对象可能会包含一些诸如FirstName, LastName, Email, Address等信息。
// 但如果UI上不打算显示Address的信息，那么CustomerDTO中也无需包含这个 Address的数据

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String username;
    private String authenticationToken;
//    private String refreshToken;
//    private Instant expiresAt;

}
