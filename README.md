# SentryLogger

SentryLogger is a plugin that integrates [Sentry](https://sentry.io) error tracking with your Paper or Velocity server.
It automatically captures exceptions and logs with context, helping you identify and fix issues more efficiently.

## Features

- Automatic exception tracking with Sentry
- Detailed error reporting with server context
- Configurable ignore list for common exceptions
- Support for both Paper and Velocity platforms
- Alerts on Discord via the Sentry platform

## Supported Platforms

- Paper 1.21+
- Velocity 3.4.0+

## Usage

1. Sign up for a [Sentry](https://sentry.io) account if you don't have one
2. Create a new project in Sentry, select the `Log4j 2.x` platform
3. Copy your DSN url from the Sentry project settings
    - You can get it by going to `Settings` in sidebar
    - click `projects`
    - click on the created project
    - near the bottom of the sidebar click on `Client Keys (DSN)`
    - You should now see the DSN url
4. Add your DSN url to the `config.conf` file
5. Reload the plugin with `/sentrylogger reload`
6. Test the integration with `/sentrylogger test`

## Commands

| Command               | Permission            | Description                                            |
|-----------------------|-----------------------|--------------------------------------------------------|
| `sentrylogger reload` | `sentrylogger.reload` | Reload the configuration file                          |
| `sentrylogger test`   | `sentrylogger.test`   | Test the Sentry integration by generating an exception |

## Building from Source

1. Clone the repository
2. Run `./gradlew build` to build the project
3. Find the built JARs in the `platform/paper/build/libs` and `platform/velocity/build/libs` directories

## License

This project is licensed under the XYZ License - see the LICENSE file for details.