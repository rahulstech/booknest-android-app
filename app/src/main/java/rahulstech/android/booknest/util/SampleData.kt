package rahulstech.android.booknest.util

import rahulstech.android.booknest.R
import rahulstech.android.booknest.ui.model.Amenity
import rahulstech.android.booknest.ui.model.FindHotelItem
import rahulstech.android.booknest.ui.model.HotelDetails
import rahulstech.android.booknest.ui.model.Place
import rahulstech.android.booknest.ui.model.PlaceDetails
import rahulstech.android.booknest.ui.model.RoomDetails
import rahulstech.android.booknest.ui.model.UserDetails

val samplePlaces = listOf(
    Place(
        id = "place-1",
        name = "Agra",
        imageUrl = "https://images.unsplash.com/photo-1564507592333-c60657eea523?w=800"
    ),
    Place(
        id = "place-2",
        name = "Bengaluru",
        imageUrl = "https://images.unsplash.com/photo-1596760411126-fa9997900cc2?w=800"
    ),
    Place(
        id = "place-3",
        name = "Chennai",
        imageUrl = "https://images.unsplash.com/photo-1582510003544-2d095665039b?w=800"
    )
)

val samplePlace = PlaceDetails(
    name = "Bengaluru",
    imageUrl = "https://images.unsplash.com/photo-1564507592333-c60657eea523?w=800",
    description = """
            Discover the vibrant cityscape of Bengaluru, known as India's Silicon Valley, 
            where the historic and contemporary seamlessly intertwine. 
            Explore the awe-inspiring Vidhana Soudha, showcasing splendid Dravidian architecture and 
            serving as the seat of the state legislature. Embrace the tranquility of Cubbon Park, 
            a lush oasis in the heart of the city, perfect for leisurely strolls amidst verdant 
            landscapes and diverse flora. Uncover the cultural richness at Bangalore Palace, 
            an architectural gem adorned with opulent interiors and a glimpse into royal heritage, 
            offering a fascinating journey through Bengaluru's past.
            """.trimIndent()
)

val sampleLocations = samplePlaces.map { it.name }

val sampleHotels = listOf(
    FindHotelItem(
        id = "hotel-1",
        hotelName = "Fairfield Hotel",
        cityName = "Agra",
        priceMin = 4_999,
        priceMax = 9_999,
        isSoldOut = false,
        imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800",
    ),
    FindHotelItem(
        id = "hotel-2",
        hotelName = "Oberoi Hotel",
        cityName = "Agra",
        isSoldOut = true,
        imageUrl = "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=800",
    ),
    FindHotelItem(
        id = "hotel-3",
        hotelName = "Taj Hotel",
        cityName = "Agra",
        isSoldOut = true,
        imageUrl = "https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=800",
    ),
)

val sampleHotel = HotelDetails(
    heroPhotoUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800",
    name = "Fairfield Hotel",
    location = "Agra",
    stars = 4.3f,
    aboutTheHotel = "Our hotel is located in Ashok Cosmos Mall in Sanjay Place, one of the " +
            "largest shopping malls in the city. Iconic sites such as the Taj Mahal are minutes " +
            "away while fun outings to Agra Golf Course are just around the corner.",
    amenities = listOf(
        Amenity(R.drawable.ic_gym, "Gym"),
        Amenity(R.drawable.ic_free_parking, "Free Parking"),
        Amenity(R.drawable.ic_restaurant, "Restaurant")
    ),
    rulesAndInformation = listOf(
        "Check-in: 12.00 Pm, Check-out: 11.00 Am",
        "Pets are not allowed.",
        "Outside food is not allowed",
        "Passport, Aadhar, and Govt. ID are accepted as ID proofs."
    )
)

val sampleRooms = listOf(
    RoomDetails(
        id = "1",
        roomType = "Deluxe Room AC",
        pricePerDay = 4999,
        photoUrl = "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=800"
    ),
    RoomDetails(
        id = "2",
        roomType = "Double Room",
        pricePerDay = 7999,
        photoUrl = "https://images.unsplash.com/photo-1618773928121-c32242e63f39?w=800"
    ),
    RoomDetails(
        id = "3",
        roomType = "Executive",
        pricePerDay = 9999,
        photoUrl = "https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=800"
    )
)

val sampleSelectedRooms = listOf(sampleRooms[0], sampleRooms[2])

val sampleUserDetails = UserDetails("John Doe", "john.doe@email.com", "9999999999")