using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

#pragma warning disable CA1814 // Prefer jagged arrays over multidimensional

namespace Entities.Migrations
{
    /// <inheritdoc />
    public partial class Initial : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "countries",
                columns: table => new
                {
                    Id = table.Column<Guid>(type: "uuid", nullable: false),
                    Name = table.Column<string>(type: "character varying(40)", maxLength: 40, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_countries", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "persons",
                columns: table => new
                {
                    Id = table.Column<Guid>(type: "uuid", nullable: false),
                    Name = table.Column<string>(type: "character varying(40)", maxLength: 40, nullable: false),
                    Email = table.Column<string>(type: "character varying(40)", maxLength: 40, nullable: false),
                    DateOfBirth = table.Column<DateTime>(type: "timestamp with time zone", nullable: true),
                    Gender = table.Column<string>(type: "character varying(10)", maxLength: 10, nullable: true),
                    CountryId = table.Column<Guid>(type: "uuid", nullable: true),
                    Address = table.Column<string>(type: "character varying(250)", maxLength: 250, nullable: true),
                    ReceiveNewsLetters = table.Column<bool>(type: "boolean", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_persons", x => x.Id);
                });

            migrationBuilder.InsertData(
                table: "countries",
                columns: new[] { "Id", "Name" },
                values: new object[,]
                {
                    { new Guid("49e3b79b-2403-4d02-9067-2ceb2cf39251"), "India" },
                    { new Guid("51b7ab30-2365-4e53-8136-6e2ea26fe139"), "Japan" },
                    { new Guid("5dd646d9-8ccb-4801-bfe8-d41881b0a656"), "Saudi Arabia" },
                    { new Guid("66111b49-5cd9-4d8f-99f8-db6c04d5e432"), "Yaman" },
                    { new Guid("c7417dd6-ab9e-4b55-8ccb-e5fe28cf236a"), "Philistine" }
                });

            migrationBuilder.InsertData(
                table: "persons",
                columns: new[] { "Id", "Address", "CountryId", "DateOfBirth", "Email", "Gender", "Name", "ReceiveNewsLetters" },
                values: new object[,]
                {
                    { new Guid("116f8e3d-c396-4759-8e45-ba7823d656a3"), "Kareli, Allahabad, U.P.", new Guid("c7417dd6-ab9e-4b55-8ccb-e5fe28cf236a"), new DateTime(2002, 10, 3, 0, 0, 0, 0, DateTimeKind.Unspecified), "imakhlaqxd@gmail.com", "Male", "Akhlaq Ahmad", false },
                    { new Guid("46f98771-9622-4e27-a160-d4941bed238a"), "2No Adda, Siwan, Bihar", new Guid("66111b49-5cd9-4d8f-99f8-db6c04d5e432"), new DateTime(2000, 12, 27, 0, 0, 0, 0, DateTimeKind.Unspecified), "imakhlaqxd123@gmail.com", "Male", "Yusuf Kamal", false },
                    { new Guid("ad57c271-c77c-4a14-965c-5dca5695ed1d"), "M.M Coloney, Siwan, Bihar", new Guid("5dd646d9-8ccb-4801-bfe8-d41881b0a656"), new DateTime(2010, 6, 2, 0, 0, 0, 0, DateTimeKind.Unspecified), "imakhlaqxd122@gmail.com", "Male", "Abdullah Bin Tahir", true },
                    { new Guid("e967510e-37cf-4aa5-8b3f-10d5389e6ebc"), "Kareli, Allahabad, U.P.", new Guid("c7417dd6-ab9e-4b55-8ccb-e5fe28cf236a"), new DateTime(2006, 11, 10, 0, 0, 0, 0, DateTimeKind.Unspecified), "imakhlaqxd22@gmail.com", "Male", "Dilshad Ahmad", false }
                });
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "countries");

            migrationBuilder.DropTable(
                name: "persons");
        }
    }
}
